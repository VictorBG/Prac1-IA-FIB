package IA.Azamon.functions.calculators;

import static IA.Azamon.Paquete.PR1;
import static IA.Azamon.Paquete.PR2;
import static IA.Azamon.Paquete.PR3;

import java.util.function.BiFunction;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;


public final class HappinessCalculator implements BiFunction<Oferta, Paquete, Integer>
{
    @Override
    public Integer apply(final Oferta offer, final Paquete pack)
    {
        if (offer == null)
        {
            return -1;
        }

        switch (pack.getPrioridad())
        {
            case PR1:
                switch (offer.getDias())
                {
                    case 1:
                        return 0;
                    case 2:
                    case 3:
                        return -1;
                    case 4:
                    case 5:
                        return -2;
                }
            case PR2:
                switch (offer.getDias())
                {
                    case 1:
                        return 1;
                    case 2:
                    case 3:
                        return 0;
                    case 4:
                    case 5:
                        return -1;
                }
            case PR3:
                switch (offer.getDias())
                {
                    case 1:
                        return 2;
                    case 2:
                    case 3:
                        return 1;
                    case 4:
                    case 5:
                        return 0;
                }
            default:
                return -10;
        }

    }
}
