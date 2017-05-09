/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.hashmap;

import edu.computerapex.hashmap.Node;
import edu.computerapex.hashmap.TreeNode;
import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class HashMap<K, V>
extends AbstractMap<K, V> {
    transient Node<K, V>[] table;
    static final transient int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int MAXIMUM_CAPACITY = 1073741824;
    static final int MIN_TREEIFY_CAPACITY = 64;
    static final int TREEIFY_THRESHOLD = 8;
    float loadFactor = 0.75f;
    transient int accommodation = 16;
    int threshold = 0;
    transient Set<Map.Entry<K, V>> entryDefine;
    transient int size;

    public HashMap() {
        this(16, 0.75f);
    }

    public HashMap(Map<? extends K, ? extends V> m) {
        this();
        this.putAll(m);
    }

    public HashMap(int accommodation) {
        this(accommodation, 0.75f);
    }

    public HashMap(int accommodation, float loadFactor) {
        this.entryDefine = new TreeSet<Map.Entry<K, V>>(new NodeComparator());
        this.size = 0;
        this.accommodation = accommodation;
        this.loadFactor = loadFactor;
        Node[] newTable = new Node[accommodation];
        this.table = newTable;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return this.entryDefine;
    }

    @Override
    public V put(K key, V value) {
        Object e = null;
        int h = this.hash(key);
        Node<K, V> node = this.table[h];
        if (node == null) {
            this.table[h] = node = new Node<K, V>(this.hash(key), key, value, null);
            this.entryDefine.add(node);
            ++this.size;
        } else if (node instanceof TreeNode) {
            TreeNode treeNode = (TreeNode)node;
            TreeNode<K, V> result = treeNode.insertTreeVal(this.table, this.hash(key), key, value);
            if (result == null) {
                this.entryDefine.add(new AbstractMap.SimpleEntry<K, V>(key, value));
                ++this.size;
                return null;
            }
            if (result.value != value) {
                this.entryDefine.remove(result);
                e = result.value;
                result.setValue(value);
                this.entryDefine.add(result);
            }
        } else {
            int bincount = 0;
            while (node.next != null && !node.key.equals(key)) {
                node = node.next;
                ++bincount;
            }
            if (node.key.equals(key)) {
                e = node.value;
                node.value = value;
            } else {
                node.next = new Node<K, V>(this.hash(key), key, value, null);
                this.entryDefine.add(node.next);
                if (bincount > 8) {
                    this.treeify(this.table, h);
                }
                ++this.size;
            }
        }
        if ((float)this.size > (float)this.accommodation * 0.75f && this.size < 1073741824) {
            this.placeAssist();
        }
        return (V)e;
    }

    private void placeAssist() {
        this.resize();
    }

    @Override
    public V get(Object key) {
        Node<K, V> node = this.fetchNode(key);
        if (node == null) {
            return null;
        }
        if (node instanceof TreeNode) {
            return new HashMapUtility((TreeNode)node).invoke();
        }
        return this.getWorker(key, node);
    }

    private V getWorker(Object key, Node<K, V> node) {
        if (node.key.equals(key)) {
            return node.value;
        }
        return null;
    }

    private Node<K, V> fetchNode(Object key) {
        int h = this.hash(key);
        Node<K, V> node = this.table[h];
        if (node == null) {
            return null;
        }
        if (node instanceof TreeNode) {
            TreeNode n = (TreeNode)node;
            if ((n = n.grabTreeNode(h, key)) == null) {
                return null;
            }
            return n;
        }
        while (node.next != null && !node.key.equals(key)) {
            node = node.next;
        }
        if (node.key.equals(key)) {
            return node;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return this.fetchNode(key) != null;
    }

    @Override
    public V remove(Object key) {
        int h = this.hash(key);
        Node<K, V> node = this.table[h];
        Node<K, V> prev = null;
        if (node == null) {
            return null;
        }
        if (node instanceof TreeNode) {
            return new HashMapSupervisor(key, h, (TreeNode)node).invoke();
        }
        while (node.next != null && !node.key.equals(key)) {
            prev = node;
            node = node.next;
        }
        if (node.key.equals(key)) {
            return this.removeHandler(h, node, prev);
        }
        return null;
    }

    private V removeHandler(int h, Node<K, V> node, Node<K, V> prev) {
        if (prev == null) {
            new HashMapService(h, node).invoke();
        } else {
            prev.next = node.next;
        }
        this.entryDefine.remove(node);
        --this.size;
        return node.value;
    }

    private int hash(Object key) {
        return Node.hash(key, this.accommodation);
    }

    final Node<K, V>[] resize() {
        int newCap;
        Node<K, V>[] oldTab = this.table;
        int oldCap = oldTab == null ? 0 : oldTab.length;
        int oldThr = this.threshold;
        int newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= 1073741824) {
                return this.resizeHome(oldTab);
            }
            newCap = oldCap << 1;
            if (newCap < 1073741824 && oldCap >= 16) {
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = 16;
            newThr = 12;
        }
        if (newThr == 0) {
            float ft = (float)newCap * this.loadFactor;
            newThr = newCap < 1073741824 && ft < 1.07374182E9f ? (int)ft : Integer.MAX_VALUE;
        }
        this.threshold = newThr;
        Node[] newTab = new Node[newCap];
        this.accommodation = newCap;
        this.table = newTab;
        if (oldTab != null) {
            Set<Map.Entry<K, V>> oldEntries = this.entrySet();
            this.entryDefine = new TreeSet<Map.Entry<K, V>>(new NodeComparator());
            for (Map.Entry<K, V> entry : oldEntries) {
                this.resizeTarget(entry);
            }
        }
        return newTab;
    }

    private void resizeTarget(Map.Entry<K, V> entry) {
        this.put(entry.getKey(), entry.getValue());
    }

    private Node<K, V>[] resizeHome(Node<K, V>[] oldTab) {
        this.threshold = Integer.MAX_VALUE;
        return oldTab;
    }

    private void treeify(Node<K, V>[] tab, int index) {
        int n;
        if (tab == null || (n = tab.length) < 64) {
            this.resize();
        } else {
            Node<K, V> e = tab[index];
            if (e != null) {
                TreeNode<K, V> hd = null;
                TreeNode<K, V> tl = null;
                do {
                    TreeNode<K, V> p = new TreeNode<K, V>(e.hash, e.key, e.value, null);
                    if (tl == null) {
                        hd = p;
                    } else {
                        this.treeifyExecutor(tl, p);
                    }
                    tl = p;
                } while ((e = e.next) != null);
                tab[index] = hd;
                if (tab[index] != null) {
                    hd.treeify(tab);
                }
            }
        }
    }

    private void treeifyExecutor(TreeNode<K, V> tl, TreeNode<K, V> p) {
        p.prev = tl;
        tl.next = p;
    }

    private class HashMapService {
        private int h;
        private Node<K, V> node;

        public HashMapService(int h, Node<K, V> node) {
            this.h = h;
            this.node = node;
        }

        public void invoke() {
            HashMap.this.table[this.h] = this.node.next;
        }
    }

    private class HashMapSupervisor {
        private Object key;
        private int h;
        private TreeNode<K, V> node;

        public HashMapSupervisor(Object key, int h, TreeNode<K, V> node) {
            this.key = key;
            this.h = h;
            this.node = node;
        }

        public V invoke() {
            TreeNode<K, V> treenode = this.node;
            TreeNode nodeToRemove = treenode.grabTreeNode(this.h, this.key);
            if (nodeToRemove == null) {
                return null;
            }
            nodeToRemove.removeTreeNode(HashMap.this.table, true);
            --HashMap.this.size;
            HashMap.this.entryDefine.remove(new AbstractMap.SimpleEntry<Object, Object>(this.key, nodeToRemove.value));
            return (V)treenode.value;
        }
    }

    private class HashMapUtility {
        private TreeNode<K, V> node;

        public HashMapUtility(TreeNode<K, V> node) {
            this.node = node;
        }

        public V invoke() {
            TreeNode<K, V> n = this.node;
            return n.getValue();
        }
    }

    class NodeComparator
    implements Comparator {
        NodeComparator() {
        }

        public int compare(Object a, Object b) {
            if (a instanceof Map.Entry && b instanceof Map.Entry) {
                Map.Entry ae = (Map.Entry)a;
                Map.Entry be = (Map.Entry)b;
                Object ak = ae.getKey();
                Object av = ae.getValue();
                Object bk = be.getKey();
                Object bv = be.getValue();
                if (ak.equals(bk) && (av == null && bv == null || av.equals(bv))) {
                    return 0;
                }
                int avHash = 0;
                int bvHash = 0;
                if (av != null) {
                    avHash = av.hashCode();
                }
                if (bv != null) {
                    bvHash = bv.hashCode();
                }
                if (ak.hashCode() < bk.hashCode() || ak.hashCode() == bk.hashCode() && avHash < bvHash) {
                    return -1;
                }
                return 1;
            }
            return Integer.compare(a.hashCode(), b.hashCode());
        }
    }

}

