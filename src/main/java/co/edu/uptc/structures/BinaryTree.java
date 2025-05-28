package co.edu.uptc.structures;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BinaryTree<T> {

    private Node<T> root;
    private Comparator<T> comparator;
    private List<T> data;

    public BinaryTree(Comparator<T> comparator) {
        root = null;
        this.comparator = comparator;
    }

    public void delete(T data) {
        root = delete(root, data);
    }

    protected Node<T> delete(Node<T> node, T data) {
        if (comparator.compare(node.getData(), data) < 0) {
            Node<T> rightNode = delete(node.getRight(), data);
            node.setRight(rightNode);
        } else {
            if (comparator.compare(node.getData(), data) > 0) {
                Node<T> leftNode = delete(node.getLeft(), data);
                node.setLeft(leftNode);
            } else {
                Node<T> q = node;
                if (q.getLeft() == null)
                    node = q.getRight();
                else
                    q = replace(q);
                q = null;
            }

        }
        return node;
    }

    private Node<T> replace(Node<T> actual) {
        Node<T> a, p;
        p = actual;
        a = actual.getLeft();
        while (a.getRight() != null) {
            p = a;
            a = a.getRight();
        }
        actual.setData(a.getData());
        if (p == actual)
            p.setLeft(a.getLeft());
        else
            p.setRight(a.getLeft());
        return a;
    }

    public List<T> pre_order() {
        data = new ArrayList<>();
        pre_order(root, data);
        return data;
    }

    public List<T> in_order() {
        data = new ArrayList<>();
        in_order(root, data);
        return data;
    }

    public List<T> post_order() {
        data = new ArrayList<>();
        post_order(root, data);
        return data;
    }

    private void post_order(Node<T> aux, List<T> data) {
        if (aux != null) {
            post_order(aux.getLeft(), data);
            post_order(aux.getRight(), data);
            data.add(aux.getData());
        }
    }

    private void in_order(Node<T> aux, List<T> data) {
        if (aux != null) {
            in_order(aux.getLeft(), data);
            data.add(aux.getData());
            in_order(aux.getRight(), data);
        }
    }

    private void pre_order(Node<T> aux, List<T> data) {
        if (aux != null) {
            data.add(aux.getData());
            pre_order(aux.getLeft(), data);
            pre_order(aux.getRight(), data);
        }
    }

    public boolean add(T data) {
        if (data == null)
            return false;
        Node<T> newNode = new Node<>(data);
        if (isEmpty()) {
            root = newNode;
            return true;
        }
        Node<T> aux = root;
        Node<T> parent = null;
        while (aux != null) {
            parent = aux;
            int cmp = comparator.compare(data, aux.getData());
            if (cmp < 0) {
                aux = aux.getLeft();
                if (aux == null) {
                    parent.setLeft(newNode);
                    return true;
                }
            } else if (cmp > 0) {
                aux = aux.getRight();
                if (aux == null) {
                    parent.setRight(newNode);
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public T exist(T data) {
        return exist(root, data);
    }

    private T exist(Node<T> node, T data) {
        if (node == null) {
            return null;
        }
        int compareResult = comparator.compare(data, node.getData());
        if (compareResult == 0) {
            return node.getData();
        } else if (compareResult < 0) {
            return exist(node.getLeft(), data);
        } else {
            return exist(node.getRight(), data);
        }
    }

    public boolean isEmpty() {
        return root == null;
    }
    
    public T search(T data) {
        return exist(root, data);
    }

}