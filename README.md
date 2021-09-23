# Azamon - Practica 1 IA

- Sergio Vázquez Montes de Oca
- Víctor Blanco García

Nota: 10


# Ejecución

Para ejecutar el programa hay que ejecutar la clase `Main`.

Al ser ejecutada, el menú de la aplicación aparecerá con las opciones:
- `Hill Climbing`
- `Simulated Annealing`
- `Experiments`

Las dos primeras opciones permiten ejecutar una simulación con el algoritmo seleccionado. Cada opción preguntará los valores de los campos requeridos para cada algoritmo y de los campos requeridos para el estado (estrategia de generación de soluciones inicial, heurístico, número de paquetes, proporción ofertas/paquetes, semilla y visibilidad de la información que aporta la ejecución, tales como los pasos ejecutados, las acciones o información general).

La tercera opción nos permite ejecutar los experimentos de forma individual o conjunta, excepto los experimentos 7 y 9 que son realizados únicamente de forma individual. Una vez han sido elegidos los experimentos a ejecutar, podemos ejecutarlos (opción `run`) o ejecutarlos y guardarlos (`run & save`), la cual guarda el resultado del experimento en un fichero Excel en la raíz del proyecto, con nombre `result` y con una hoja por cada experimento ejecutado.
