package IA.Azamon.utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class Utils
{
    public static <T> boolean isInsideBounds(List<T> list, int position)
    {
        return position >= 0 && position < list.size();
    }

    public static <T> boolean isInsideBounds(T[] list, int position)
    {
        return position >= 0 && position < list.length;
    }

    public static <T> T safeGet(T[] list, int position)
    {
        if (isInsideBounds(list, position))
        {
            return list[position];
        }
        return null;
    }

    public static <T> T safeGet(List<T> list, int position)
    {
        if (isInsideBounds(list, position))
        {
            return list.get(position);
        }
        return null;
    }

    public static long countExecutionTime(final Callable<Void> runnable) throws Exception
    {
        return countExecutionTime(runnable, false);
    }

    public static long countExecutionTime(final Callable<Void> runnable, boolean printTime) throws Exception
    {
        long time = System.currentTimeMillis();
        runnable.call();
        time = System.currentTimeMillis() - time;
        if (printTime)
        {
            System.out.printf("Execution time: %dms", time);
        }
        return time;
    }

    public static boolean isAcceptable(final int prio, final int days)
    {
        switch (prio)
        {
            case 0:
                return days <= 1;
            case 1:
                return days <= 3;
            case 2:
                return days <= 5;
            default:
                return false;
        }
    }

    public static List<Integer> getSeeds() {
        return Stream.of(1054626374,
            205601455,
            -580891368,
            166733950,
            486734037,
            1803997614,
            -614522290,
            1946948947,
            -2126737832,
            1401079494,
            738425236,
            156098020,
            198917131,
            -1059536367,
            809642523).collect(
            Collectors.toList());
    }
}
