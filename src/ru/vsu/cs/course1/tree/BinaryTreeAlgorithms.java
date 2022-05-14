package ru.vsu.cs.course1.tree;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class BinaryTreeAlgorithms {
    public static class PairOfNodes<T> {
        private final T first;
        private final T second;
        private final int firstLevel;
        private final int secondLevel;

        public PairOfNodes(T first, int firstLevel, T second, int secondLevel) {
            this.first = first;
            this.second = second;
            this.firstLevel = firstLevel;
            this.secondLevel = secondLevel;
        }

        public PairOfNodes() {
            this(null, -1, null, -1);
        }

        public T getFirst() {
            return first;
        }

        public T getSecond() {
            return second;
        }

        public int getFirstLevel() {
            return firstLevel;
        }

        public int getSecondLevel() {
            return secondLevel;
        }
    }

    @FunctionalInterface
    public interface MyVisitor<T> {
        void visit(BinaryTree.TreeNode<T> node, int level);
    }


    @FunctionalInterface
    public interface Visitor<T> {
        /**
         * "Посещение" значения
         *
         * @param value Значение, которое "посещаем"
         * @param level Уровень дерева/поддерева, на котором находится данное значение
         */
        void visit(T value, int level);
    }


    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в прямом/NLR порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor Посетитель
     */
    public static <T> void preOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        class Inner {
            void preOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                if (node == null) {
                    return;
                }
                visitor.visit(node.getValue(), level);
                preOrderVisit(node.getLeft(), visitor, level + 1);
                preOrderVisit(node.getRight(), visitor, level + 1);
            }
        }
        new Inner().preOrderVisit(treeNode, visitor, 0);
    }

    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в симметричном/поперечном/центрированном/LNR порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor Посетитель
     */
    public static <T> void inOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        class Inner {
            void inOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                if (node == null) {
                    return;
                }
                inOrderVisit(node.getLeft(), visitor, level + 1);
                visitor.visit(node.getValue(), level);
                inOrderVisit(node.getRight(), visitor, level + 1);
            }
        }
        new Inner().inOrderVisit(treeNode, visitor, 0);
    }

    /**
     * Обход поддерева с вершиной в данном узле
     * "посетителем" в обратном/LRN порядке - рекурсивная реализация
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor Посетитель
     */
    public static <T> void postOrderVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        class Inner {
            void postOrderVisit(BinaryTree.TreeNode<T> node, Visitor<T> visitor, int level) {
                if (node == null) {
                    return;
                }
                postOrderVisit(node.getLeft(), visitor, level + 1);
                postOrderVisit(node.getRight(), visitor, level + 1);
                visitor.visit(node.getValue(), level);
            }
        }
        new Inner().postOrderVisit(treeNode, visitor, 0);
    }

    /**
     *  Класс для хранения узла дерева вместе с его уровнем, нужен для методов
     *  {@link #byLevelVisit(BinaryTree.TreeNode, Visitor)}
     *
     * @param <T>
     */
    private static class QueueItem<T> {
        public BinaryTree.TreeNode<T> node;
        public int level;

        public QueueItem(BinaryTree.TreeNode<T> node, int level) {
            this.node = node;
            this.level = level;
        }
    }

    /**
     * Обход поддерева с вершиной в данном узле "посетителем" по уровням (обход в ширину)
     *
     * @param treeNode Узел поддерева, которое требуется "обойти"
     * @param visitor Посетитель
     */
    public static <T> void byLevelVisit(BinaryTree.TreeNode<T> treeNode, Visitor<T> visitor) {
        Queue<QueueItem<T>> queue = new LinkedList<>();
        queue.add(new QueueItem<>(treeNode, 0));
        while (!queue.isEmpty()) {
            QueueItem<T> item = queue.poll();
            if (item.node.getLeft() != null) {
                queue.add(new QueueItem<>(item.node.getLeft(), item.level + 1));
            }
            if (item.node.getRight() != null) {
                queue.add(new QueueItem<>(item.node.getRight(), item.level + 1));
            }
            visitor.visit(item.node.getValue(), item.level);
        }
    }

    public static <T> List<PairOfNodes<T>> findPares(BinaryTree.TreeNode<T> root) {
        List<PairOfNodes<T>> pairs = new ArrayList<>();
        List<BinaryTree.TreeNode<T>> roots = new ArrayList<>();
        List<Integer> levels = new ArrayList<>();
        rootsTraversal(root, roots, levels, 0);
        roots.remove(0);
        levels.remove(0);
        for (int i = 0; i < roots.size() - 1; i++) {
            for (int j = i + 1; j < roots.size(); j++) {
                if (isSubtreesEqual(roots.get(i), roots.get(j))) {
                    pairs.add(new PairOfNodes<>(roots.get(i).getValue(), levels.get(i), roots.get(j).getValue(), levels.get(j)));
                }
            }
        }
        return pairs;
    }

    private static <T> void rootsTraversal(BinaryTree.TreeNode<T> node, List<BinaryTree.TreeNode<T>> roots, List<Integer> levels, int level) {
        if (node.getLeft() == null && node.getRight() == null) {
            return;
        }
        roots.add(node);
        levels.add(level);
        if (node.getLeft() != null) {
            rootsTraversal(node.getLeft(), roots, levels, level + 1);
        }
        if (node.getRight() != null) {
            rootsTraversal(node.getRight(), roots, levels, level + 1);
        }
    }

    private static <T> boolean isSubtreesEqual(BinaryTree.TreeNode<T> first, BinaryTree.TreeNode<T> second) {
        if (first == null && second == null) {
            return true;
        }
        if (first != null && second != null) {
            if (first.hasChild() || second.hasChild()) {
                return isSubtreesEqual(first.getLeft(), second.getLeft()) && isSubtreesEqual(first.getRight(), second.getRight());
            } else {
                return first.getValue() == second.getValue();
            }
        } else {
            return false;
        }
    }
}
