package IA.Azamon.functions.calculators;

import java.util.function.BiFunction;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;


public class PriceCalculator implements BiFunction<Oferta, Paquete, Double>
{
    private final static double PRICE_PER_KG_AND_DAY = 0.25;

    private final static double DAYS_STORAGE_2_3_DAYS = 1;
    private final static double DAYS_STORAGE_4_5_DAYS = 2;

    @Override
    public Double apply(final Oferta offer, final Paquete pack)
    {
        double extra = 0.;
        switch (offer.getDias())
        {
            case 3:
            case 4:
                extra += PRICE_PER_KG_AND_DAY * pack.getPeso() * DAYS_STORAGE_2_3_DAYS;
                break;
            case 5:
                extra += PRICE_PER_KG_AND_DAY * pack.getPeso() * DAYS_STORAGE_4_5_DAYS;
        }

        return offer.getPrecio() * pack.getPeso() +  extra;
    }
}
