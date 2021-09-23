package IA.Azamon.functions.successors;

import java.util.ArrayList;
import java.util.List;

import IA.Azamon.AzamonState;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;


public class AzamonSuccessorFunctionHC implements SuccessorFunction
{
    public static boolean USE_MOVE = true;
    public static boolean USE_SWAP = true;

    @Override
    public List<Successor> getSuccessors(final Object o)
    {
        final List<Successor> result = new ArrayList<>();

        if (o instanceof AzamonState)
        {
            final AzamonState originalState = (AzamonState) o;

            for (int i = 0; i < AzamonState.PACKAGES.size(); i++)
            {
                if (USE_MOVE)
                {
                    for (int j = 0; j < AzamonState.TRANSPORT.size(); j++)
                    {
                        final AzamonState successor = originalState.shallowCopy();
                        if (successor.movePackage(i, j))
                        {
                            result.add(new Successor(String.format("Moved package %d to offer %d", i, j), successor));
                        }
                    }
                }

                if (USE_SWAP)
                {
                    for (int k = 0; k < AzamonState.PACKAGES.size(); k++)
                    {
                        final AzamonState successor = originalState.shallowCopy();
                        if (successor.swapPackages(i, k))
                        {
                            result.add(new Successor(String.format("Swapped package %d with package %d", i, k), successor));
                        }
                    }
                }
            }
        }
        return result;
    }
}
