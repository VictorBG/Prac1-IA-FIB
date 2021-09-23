package IA.Azamon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import IA.Azamon.functions.calculators.HappinessCalculator;
import IA.Azamon.functions.calculators.PriceCalculator;
import IA.Azamon.functions.initial.InitialSolutionFunction;
import IA.Azamon.utils.Pair;
import IA.Azamon.utils.Utils;


public final class AzamonState
{
    private static final int NOT_ASSIGNED = -1;

    public static Transporte TRANSPORT;
    public static Paquetes PACKAGES;

    /**
     * Maps 1 package to 1 assignment, the position x is the position x of the package in PACKAGES and the value is the offer in TRANSPORT
     */
    private final List<Integer> packageOfferMap;

    private BiFunction<Oferta, Paquete, Integer> happinessCalculator = new HappinessCalculator();
    private double happiness;

    private BiFunction<Oferta, Paquete, Double> priceCalculator = new PriceCalculator();
    private double price;

    private final List<Double> capacityRemaining;

    public AzamonState(final int numPackages, final int seedPackages, final double proportionTransport, final int seedTransport)
    {
        PACKAGES = new Paquetes(numPackages, seedPackages);
        TRANSPORT = new Transporte(PACKAGES, proportionTransport, seedTransport);

        // Init assignments
        packageOfferMap = PACKAGES.stream().map(i -> NOT_ASSIGNED).collect(Collectors.toList());

        // Init capacity
        capacityRemaining = TRANSPORT.stream().map(Oferta::getPesomax).collect(Collectors.toList());
    }

    private AzamonState(final AzamonState original)
    {
        this.packageOfferMap = new ArrayList<>(original.packageOfferMap);

        this.happinessCalculator = original.happinessCalculator;
        this.happiness = original.happiness;

        this.priceCalculator = original.priceCalculator;
        this.price = original.price;

        this.capacityRemaining = new ArrayList<>(original.capacityRemaining);
    }

    // region Structure access
    private Oferta getOffer(int offer)
    {
        return Utils.safeGet(TRANSPORT, offer);
    }

    private Paquete getPackage(int packageNumber)
    {
        return Utils.safeGet(PACKAGES, packageNumber);
    }

    private Oferta getAssignedOffer(int packageNumber)
    {
        return getOffer(getAssignedOfferNumber(packageNumber));
    }

    private int getAssignedOfferNumber(int packageNumber)
    {
        return Utils.safeGet(packageOfferMap, packageNumber);
    }

    private int getSize()
    {
        return packageOfferMap.size();
    }

    private Map<Oferta, List<Pair<Integer, Paquete>>> getMapping()
    {
        final Map<Oferta, List<Pair<Integer, Paquete>>> result = new HashMap<>();
        for (int i = 0; i < getSize(); i++)
        {
            if (result.containsKey(getAssignedOffer(i)))
            {
                result.get(getAssignedOffer(i)).add(new Pair<>(i, getPackage(i)));
            }
            else
            {
                final int finalI = i;
                result.put(getAssignedOffer(i), new ArrayList<Pair<Integer, Paquete>>()
                {{
                    add(new Pair<>(finalI, getPackage(finalI)));
                }});
            }
        }
        return result;
    }

    public double getRemainingCapacity(final int offerNumber)
    {
        return capacityRemaining.get(offerNumber);
    }

    private boolean put(int packageNumber, int offerNumber)
    {
        if (Utils.isInsideBounds(packageOfferMap, packageNumber) && Utils.isInsideBounds(TRANSPORT, offerNumber))
        {
            final int oldAssignedOffer = getAssignedOfferNumber(packageNumber);

            if (oldAssignedOffer != NOT_ASSIGNED)
            {
                price -= getPrice(packageNumber, oldAssignedOffer);
                happiness -= getPackageHappiness(packageNumber, oldAssignedOffer);

                capacityRemaining.set(oldAssignedOffer, capacityRemaining.get(oldAssignedOffer) + getPackage(packageNumber).getPeso());
            }
            capacityRemaining.set(offerNumber, capacityRemaining.get(offerNumber) - getPackage(packageNumber).getPeso());
            packageOfferMap.set(packageNumber, offerNumber);

            price += getPrice(packageNumber, offerNumber);
            happiness += getPackageHappiness(packageNumber, offerNumber);

            return true;
        }
        return false;
    }

    private double getPrice(int packageNumber, int offerNumber)
    {
        return priceCalculator.apply(getOffer(offerNumber), getPackage(packageNumber));
    }

    private double getPackageHappiness(int packageNumber, int offerNumber)
    {
        return happinessCalculator.apply(getOffer(offerNumber), getPackage(packageNumber));
    }

    //endregion

    // region Initial solution
    public void generateSolution(final InitialSolutionFunction solutionFunction)
    {
        solutionFunction.generateSolution(this);
    }

    // endregion

    //region Operators
    public boolean movePackage(int packageNumber, int offerNumber)
    {
        if (getAssignedOfferNumber(packageNumber) == offerNumber)
        {
            return false;
        }
        if (getRemainingCapacity(offerNumber) >= getPackage(packageNumber).getPeso() &&
            Utils.isAcceptable(getPackage(packageNumber).getPrioridad(), getOffer(offerNumber).getDias()))
        {
            return put(packageNumber, offerNumber);
        }

        return false;
    }

    public boolean swapPackages(int p1, int p2)
    {
        int p1AssignedNumber = getAssignedOfferNumber(p1);
        int p2AssignedNumber = getAssignedOfferNumber(p2);

        if (p1AssignedNumber == p2AssignedNumber)
        {
            return false;
        }

        if (p1AssignedNumber == NOT_ASSIGNED || p2AssignedNumber == NOT_ASSIGNED)
        {
            return false;
        }

        if (getRemainingCapacity(p2AssignedNumber) + getPackage(p2).getPeso() >= getPackage(p1).getPeso()
            && getRemainingCapacity(p1AssignedNumber) + getPackage(p1).getPeso() >= getPackage(p2).getPeso()
            && Utils.isAcceptable(getPackage(p2).getPrioridad(), getOffer(p1AssignedNumber).getDias())
            && Utils.isAcceptable(getPackage(p1).getPrioridad(), getOffer(p2AssignedNumber).getDias()))
        {
            return put(p1, p2AssignedNumber) && put(p2, p1AssignedNumber);
        }
        return false;
    }

    //endregion

    public double getHappiness()
    {
        return happiness;
    }

    public double getTotalPrice()
    {
        return price;
    }

    public AzamonState shallowCopy()
    {
        return new AzamonState(this);
    }

    @Override
    public String toString()
    {
        final StringBuilder stringBuilder = new StringBuilder();
        final Map<Oferta, List<Pair<Integer, Paquete>>> mapping = getMapping();
        double totalCost = 0D;
        for (Oferta offer : mapping.keySet())
        {
            double remaining = offer == null ? 0D : offer.getPesomax();
            double price = 0D;
            stringBuilder.append(offer == null ? "Storage" : offer.toString()).append("\n");
            for (Pair<Integer, Paquete> pair : mapping.get(offer))
            {
                Paquete pack = pair.getValue();
                stringBuilder.append("\t[#").append(pair.getKey()).append("] ").append(pack.toString()).append("\n");
                remaining -= pack.getPeso();
                price += priceCalculator.apply(offer, pack);
            }
            stringBuilder.append("\t").append("Capacity remaining: ").append(remaining).append("\n")
                .append("\t").append("Total cost: ").append(price).append("\n\n");
            if (offer != null)
            {
                totalCost += price + offer.getPrecio() * (offer.getPesomax() - remaining);
            }
        }
        stringBuilder.append("Total cost: ").append(price).append("\n\n");
        return stringBuilder.toString();
    }

    public Double getItemsInStorage()
    {
        final Map<Oferta, List<Pair<Integer, Paquete>>> mapping = getMapping();
        return (double) mapping.getOrDefault(null, Collections.emptyList()).size();
    }
}
