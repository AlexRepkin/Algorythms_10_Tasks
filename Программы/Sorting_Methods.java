import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Random;

public class Sorting_Methods {

    // Количество элементов в массивах.
    static int arraySize = 1000;

    // Результаты для каждого метода сортировки.
    static StringBuilder resultsQuickSort = new StringBuilder();
    static StringBuilder resultsMergeSort = new StringBuilder();
    static StringBuilder resultsHeapSort = new StringBuilder();
    static StringBuilder resultsHeapSortUnoptimised = new StringBuilder();

    // Массивы с числами в разном порядке - рандомно, от меньшего к большему и от большего к меньшему.
    static ArrayList<Integer> randomArray = new ArrayList<>();
    static ArrayList<Integer> increasingArray = new ArrayList<>();
    static ArrayList<Integer> decreasingArray = new ArrayList<>();

    /* Функция разделения для быстрой сортировки.
    Выбирается элемент (x), относительно которого произойдёт разделение на
    элементы, которые меньше или равны x, и элементы, которые больше x.
    Переменная i нужна, чтобы знать, где последний элемент (Который меньше или равен x).
    Когда весь массив просмотрен, i меняется местами с x.
     */
    static int partition(ArrayList<Integer> array, int low, int high) {
        int x = array.get(low);
        int i = low;
        for (int j = low + 1; j <= high; j++) {
            if (array.get(j) <= x) {
                i++;
                int swapping = array.get(i);
                array.set(i, array.get(j));
                array.set(j, swapping);
            }
        }
        int swapping = array.get(low);
        array.set(low, array.get(i));
        array.set(i, swapping);
        return i;
    }

    /* Быстрая сортировка, время работы в худшем случае колеблется от O(n * log(n)) до O(n^2).
    Исходный массив разделяется в partition на две части (Подробнее я указал в комментарии
    над самим partition). Полученные две части опять делятся и так до тех пор, пока не будет отсортирован весь массив.
     */
    static void quickSort(ArrayList<Integer> array, int low, int high) {
        while (low < high) {
            int partitionIndex = partition(array, low, high);
            quickSort(array, low, partitionIndex - 1);
            low = partitionIndex + 1;
        }
    }

    /* Слияние двух массивов.
    Тут объединяются два упорядоченных массива в один. Пришлось использовать два дополнительных массива:
    leftArray и rightArray для временного хранения левой и правой частей. Сливаются они в изначальный массив array.
     */
    static void merge(ArrayList<Integer> array, int start, int middle, int finish) {
        int n1 = middle - start + 1;
        int n2 = finish - middle;
        ArrayList<Integer> leftArray = new ArrayList<>(); // Массив элементов слева.
        ArrayList<Integer> rightArray = new ArrayList<>(); // Массив элементов справа.
        // Вносим нужные данные из основного массива в новые массивы.
        for (int i = 0; i < n1; ++i) leftArray.add(array.get(start + i));
        for (int j = 0; j < n2; ++j) rightArray.add(array.get(middle + 1 + j));
        int i = 0, j = 0;
        int k = start;
        // Объединение полученных массивов в единое целое.
        while (i < n1 && j < n2) {
            // Если элемент из массива слева меньше или равен элементу из массива чисел справа, то элемент из
            // массива чисел слева от центра переходит в основной массив, иначе - из массива чисел справа от центра.
            if (leftArray.get(i) <= rightArray.get(j)) {
                array.set(k, leftArray.get(i));
                i++;
            } else {
                array.set(k, rightArray.get(j));
                j++;
            }
            k++;
        }
        // Ещё есть элементы в массиве чисел слева от центра? Добавляем их в основной массив.
        while (i < n1) {
            array.set(k, leftArray.get(i));
            i++;
            k++;
        }
        // Аналогично, если ещё есть элементы в массиве чисел справа от центра, то и их добавляем в основной массив.
        while (j < n2) {
            array.set(k, rightArray.get(j));
            j++;
            k++;
        }
    }

    /* Сортировка слиянием, время работы = O (n * log(n)).
    Тут происходит разделение массива на две части (От start до middle и от middle до finish), которые
    сортируются по отдельности, после чего их объединяет функция merge.
    Повторяется, пока весь массив не будет отсортирован.
     */
    static void mergeSort(ArrayList<Integer> array, int start, int finish) {
        if (start < finish) {
            int middle = (start + finish) / 2;
            mergeSort(array, start, middle);
            mergeSort(array, middle + 1, finish);
            merge(array, start, middle, finish);
        }
    }

    /* Погружение элемента вниз в куче.
    Используется для восстановления свойства кучи после удаления максимального элемента.
    Выбирает наибольший из узла и его двух дочерних узлов, затем
    меняет их местами при необходимости и рекурсивно продолжает процесс.
     */
    private static void siftDown(ArrayList<Integer> heap, int size, int i) {
        int largest = i;
        int start = 2 * i + 1;
        int finish = 2 * i + 2;
        // Проверка, есть ли потомок слева, и больше ли он текущего наиб. элемента. Если да - он становится наибольшим.
        if (start < size && heap.get(start) > heap.get(largest)) largest = start;
        // Проверка, есть ли потомок справа, и больше ли он текущего наиб. элемента. Если да - он становится наибольшим.
        if (finish < size && heap.get(finish) > heap.get(largest)) largest = finish;
        // Если наибольший элемент не рассматриваемый элемент (i), то меняем их местами.
        if (largest != i) {
            int temp = heap.get(i);
            heap.set(i, heap.get(largest));
            heap.set(largest, temp);
            // Проверка поддерева.
            siftDown(heap, size, largest);
        }
    }

    // Сортировка кучей на месте, затраты времени = O(n * log(n)).
    static void heapSort(ArrayList<Integer> array) {
        int size = arraySize;
        // Построение кучи.
        for (int i = size / 2 - 1; i >= 0; i--) siftDown(array, size, i);
        // Извлечение элементов из кучи и помещение их в конец массива.
        for (int i = size - 1; i > 0; i--) {
            int swapping = array.get(0);
            array.set(0, array.get(i));
            array.set(i, swapping);
            siftDown(array, i, 0);
        }
    }

    /* Сортировка кучей, НЕ(!)оптимизированная, затраты времени примерно = O(n * log(n)).
    Тут использована стандартная реализация кучи (PriorityQueue) для сортировки полученного массива.
    После сортировки элементы извлекаются из кучи и помещаются обратно в массив.
    */
    static void heapSortUnoptimised(ArrayList<Integer> array) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int element : array) minHeap.offer(element);
        for (int i = 0; i < arraySize; i++) array.set(i, minHeap.poll());
    }

    // Измерение времени выполнения сортировок.
    static long measureTime(String method, ArrayList<Integer> array) {
        long startTime = System.nanoTime();
        if (Objects.equals(method, "quick")) quickSort(array, 0, arraySize - 1);
        else if (Objects.equals(method, "heap_un")) heapSortUnoptimised(array);
        else if (Objects.equals(method, "heap")) heapSort(array);
        else if (Objects.equals(method, "merge")) mergeSort(array, 0, arraySize - 1);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    // Сохранение результатов в файлы.
    static void saveResults(String fileName, StringBuilder results) {
        File outputFile = new File(fileName);
        try (OutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(results.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Random rand = new Random();
        while (arraySize < 5020) {
            // Заполнение массивов случайными числами, упорядоченными по возрастанию и упорядоченными по убыванию.
            for (int i = 0; i < arraySize; i++) {
                randomArray.add(rand.nextInt(arraySize));
                decreasingArray.add(arraySize - i);
                increasingArray.add(i);
            }

            // Копии массивов, чтобы потом можно было восстановить значения.
            ArrayList<Integer> clonedRandomArray = new ArrayList<>(randomArray);
            ArrayList<Integer> clonedIncreasingArray = new ArrayList<>(increasingArray);
            ArrayList<Integer> clonedDecreasingArray = new ArrayList<>(decreasingArray);

            // Измерение времени для быстрой сортировки.
            long spentTime = measureTime("quick", randomArray);
            resultsQuickSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("quick", increasingArray);
            resultsQuickSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("quick", decreasingArray);
            resultsQuickSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append("\n");

            // Восстановление исходных данных.
            randomArray = new ArrayList<>(clonedRandomArray);
            increasingArray = new ArrayList<>(clonedIncreasingArray);
            decreasingArray = new ArrayList<>(clonedDecreasingArray);

            // Измерение времени для сортировки слиянием.
            spentTime = measureTime("merge", randomArray);
            resultsMergeSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("merge", increasingArray);
            resultsMergeSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("merge", decreasingArray);
            resultsMergeSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append("\n");

            randomArray = new ArrayList<>(clonedRandomArray);
            increasingArray = new ArrayList<>(clonedIncreasingArray);
            decreasingArray = new ArrayList<>(clonedDecreasingArray);

            // Измерение времени для не оптимизированной сортировки кучей.
            spentTime = measureTime("heap_un", randomArray);
            resultsHeapSortUnoptimised.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("heap_un", increasingArray);
            resultsHeapSortUnoptimised.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("heap_un", decreasingArray);
            resultsHeapSortUnoptimised.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append("\n");

            randomArray = new ArrayList<>(clonedRandomArray);
            increasingArray = new ArrayList<>(clonedIncreasingArray);
            decreasingArray = new ArrayList<>(clonedDecreasingArray);

            // Измерение времени для оптимизированной сортировки кучей.
            spentTime = measureTime("heap", randomArray);
            resultsHeapSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("heap", increasingArray);
            resultsHeapSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append(";");
            spentTime = measureTime("heap", decreasingArray);
            resultsHeapSort.append(spentTime / 1000000).append(",").append((spentTime / 10000) % 100).append("\n");
            arraySize += 20;
        }
        // Просто проверка правильности работы алгоритмов.
//        ArrayList<Integer> array = new ArrayList<>();
//        arraySize = 120;
//        for (int i = 0; i < arraySize; i++) array.add(rand.nextInt(arraySize - i));
//        System.out.println("Original array " + array);
//        ArrayList<Integer> clonedRandomArray = new ArrayList<>(array);
//        quickSort(array, 0, arraySize - 1);
//        System.out.println("Quick Sort " + array);
//        array = new ArrayList<>(clonedRandomArray);
//        heapSort(array);
//        System.out.println("Heap Sort " + array);
//        array = new ArrayList<>(clonedRandomArray);
//        heapSortUnoptimised(array);
//        System.out.println("Unoptimised Heap Sort " + array);
//        array = new ArrayList<>(clonedRandomArray);
//        mergeSort(array, 0, arraySize - 1);
//        System.out.println("Merge Sort " + array);

        // Сохранение результатов в файлы.
        saveResults("Results Quick Sort.txt", resultsQuickSort);
        saveResults("Results Merge Sort.txt", resultsMergeSort);
        saveResults("Results Heap Sort Unoptimised.txt", resultsHeapSortUnoptimised);
        saveResults("Results Heap Sort.txt", resultsHeapSort);
    }
}
