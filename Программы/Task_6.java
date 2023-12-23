import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Task_6 {
    // Метод для вычисления и вывода отсортированных сумм элементов двух списков
    public static void calculatingSums(ArrayList<Integer> first, ArrayList<Integer> second) {
        if (first.isEmpty() || second.isEmpty()) return;
        // Сортировка обоих списков по возрастанию
        first.sort(Comparator.naturalOrder());
        second.sort(Comparator.naturalOrder());
        // Приоритетная очередь для отслеживания минимальных сумм с их индексами
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        // Множество для отслеживания уже рассмотренных индексов
        HashSet<int[]> used = new HashSet<>();
        // Добавление первой суммы в очередь и множество
        heap.add(new int[]{first.get(0) + second.get(0), 0, 0});
        used.add(new int[]{0, 0});
        for (int count = 0; count < first.size() * second.size(); count++) {
            // Извлечение минимальной суммы из очереди
            int[] current = heap.poll();
            int sum = current[0];
            int i = current[1];
            int j = current[2];
            // Удаление уже полученных только что индексов из множества
            used.remove(new int[]{i, j});
            // Вывод текущей суммы
            System.out.print(sum + ", ");
            // Проверка и добавление следующих сумм, если индексы не рассмотрены
            if (i + 1 < first.size() && !used.contains(new int[]{i + 1, j})) {
                heap.add(new int[]{first.get(i + 1) + second.get(j), i + 1, j});
                used.add(new int[]{i + 1, j});
            }
            if (j + 1 < second.size() && !used.contains(new int[]{i, j + 1})) {
                heap.add(new int[]{first.get(i) + second.get(j + 1), i, j + 1});
                used.add(new int[]{i, j + 1});
            }
        }
    }

    public static void main(String[] args) {
        int needed = 6;
        // Создание и заполнение двух списков случайными числами от 1 до 100
        ArrayList<Integer> first = new ArrayList<>();
        ArrayList<Integer> second = new ArrayList<>();
        for (int i = 0; i < needed; i++) {
            first.add((int) (Math.random() * 100) + 1);
            second.add((int) (Math.random() * 100) + 1);
        }
        System.out.println("Good day! Lists are:\nFirst: " + first + "\nSecond: " + second + "\nSumms are: ");
        calculatingSums(first, second);
    }
}
