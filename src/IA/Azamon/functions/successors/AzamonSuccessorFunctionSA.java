package IA.Azamon.functions.successors;

import static IA.Azamon.functions.successors.AzamonSuccessorFunctionHC.USE_MOVE;
import static IA.Azamon.functions.successors.AzamonSuccessorFunctionHC.USE_SWAP;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import IA.Azamon.AzamonState;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


public class AzamonSuccessorFunctionSA implements SuccessorFunction
{
    @Override
    public List<Successor> getSuccessors(final Object o)
    {
        final List<Successor> result = new ArrayList<>();

        if (o instanceof AzamonState)
        {
            final AzamonState originalState = (AzamonState) o;
            for (; ; )
            {
                int packageNumber = new Random().nextInt(AzamonState.PACKAGES.size());
                int transportNumber = new Random().nextInt(AzamonState.TRANSPORT.size());

                final AzamonState successor = originalState.shallowCopy();

                if (USE_MOVE && !USE_SWAP)
                {
                    if (successor.movePackage(packageNumber, transportNumber))
                    {
                        result.add(new Successor(String.format("Moved package %d to offer %d", packageNumber, transportNumber), successor));
                        return result;
                    }
                }
                else if (USE_SWAP && !USE_MOVE)
                {
                    int packageNumber2 = new Random().nextInt(AzamonState.PACKAGES.size());
                    if (successor.swapPackages(packageNumber, packageNumber2))
                    {
                        result.add(new Successor(String.format("Swapped package %d to offer %d", packageNumber, transportNumber), successor));
                        return result;
                    }
                }
                else
                {
                    int operator = new Random().nextInt() % 2;
                    if (operator == 0)
                    {
                        if (successor.movePackage(packageNumber, transportNumber))
                        {
                            result.add(new Successor(String.format("Moved package %d to offer %d", packageNumber, transportNumber), successor));
                            return result;
                        }
                    }
                    else
                    {
                        int packageNumber2 = new Random().nextInt(AzamonState.PACKAGES.size());
                        if (successor.swapPackages(packageNumber, packageNumber2))
                        {
                            result.add(new Successor(String.format("Swapped package %d to offer %d", packageNumber, transportNumber), successor));
                            return result;
                        }
                    }
                }
            }
        }
        return result;
    }
}
