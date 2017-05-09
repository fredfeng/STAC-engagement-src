/*
 * Decompiled with CFR 0_121.
 */
package edu.computerapex.hashmap;

import edu.computerapex.hashmap.HashMap;
import edu.computerapex.hashmap.Node;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

final class TreeNode<K, V>
extends Node<K, V> {
    final int UNTREEIFY_THRESHOLD = 6;
    TreeNode<K, V> parent;
    TreeNode<K, V> front;
    TreeNode<K, V> last;
    TreeNode<K, V> prev;
    boolean red;

    TreeNode(int hash, K key, V val, Node<K, V> next) {
        super(hash, key, val, next);
    }

    final TreeNode<K, V> root() {
        TreeNode<K, V> r = this;
        TreeNode<K, V> p;
        while ((p = r.parent) != null) {
            r = p;
        }
        return r;
    }

    static <K, V> void moveRootToFront(Node<K, V>[] tab, TreeNode<K, V> root) {
        int n;
        if (root != null && tab != null && (n = tab.length) > 0) {
            int index = n - 1 & root.hash;
            TreeNode first = (TreeNode)tab[index];
            if (root != first) {
                tab[index] = root;
                TreeNode<K, V> rp = root.prev;
                Node rn = root.next;
                if (rn != null) {
                    ((TreeNode)rn).prev = rp;
                }
                if (rp != null) {
                    rp.next = rn;
                }
                if (first != null) {
                    first.prev = root;
                }
                root.next = first;
                root.prev = null;
            }
            assert (TreeNode.checkInvariants(root));
        }
    }

    final TreeNode<K, V> detect(int h, Object k, Class<?> kc) {
        TreeNode<K, V> p = this;
        do {
            int dir;
            TreeNode<K, V> pl = p.front;
            TreeNode<K, V> pr = p.last;
            int ph = p.hash;
            if (ph > h) {
                p = pl;
                continue;
            }
            if (ph < h) {
                p = pr;
                continue;
            }
            Object pk = p.key;
            if (pk == k || k != null && k.equals(pk)) {
                return p;
            }
            if (pl == null) {
                p = pr;
                continue;
            }
            if (pr == null) {
                p = pl;
                continue;
            }
            if ((kc != null || (kc = this.comparableClassFor(k)) != null) && (dir = this.compareComparables(kc, k, pk)) != 0) {
                p = dir < 0 ? pl : pr;
                continue;
            }
            TreeNode<K, V> q = pr.detect(h, k, kc);
            if (q != null) {
                return q;
            }
            p = pl;
        } while (p != null);
        return null;
    }

    final TreeNode<K, V> grabTreeNode(int h, Object k) {
        return (this.parent != null ? this.root() : this).detect(h, k, null);
    }

    static int tieBreakOrder(Object a, Object b) {
        int d;
        if (a == null || b == null || (d = a.getClass().getName().compareTo(b.getClass().getName())) == 0) {
            d = System.identityHashCode(a) <= System.identityHashCode(b) ? -1 : 1;
        }
        return d;
    }

    final void treeify(Node<K, V>[] tab) {
        TreeNode<K, V> root = null;
        TreeNode<K, V> x = this;
        while (x != null) {
            TreeNode next = (TreeNode)x.next;
            x.last = null;
            x.front = null;
            if (root == null) {
                x.parent = null;
                x.red = false;
                root = x;
            } else {
                int dir;
                TreeNode<K, V> xp;
                Object k = x.key;
                int h = x.hash;
                Class kc = null;
                TreeNode<K, V> p = root;
                do {
                    Object pk = p.key;
                    int ph = p.hash;
                    if (ph > h) {
                        dir = -1;
                    } else if (ph < h) {
                        dir = 1;
                    } else if (kc == null && (kc = this.comparableClassFor(k)) == null || (dir = this.compareComparables(kc, k, pk)) == 0) {
                        dir = TreeNode.tieBreakOrder(k, pk);
                    }
                    xp = p;
                } while ((p = dir <= 0 ? p.front : p.last) != null);
                x.parent = xp;
                if (dir <= 0) {
                    xp.front = x;
                } else {
                    xp.last = x;
                }
                root = TreeNode.balanceInsertion(root, x);
            }
            x = next;
        }
        TreeNode.moveRootToFront(tab, root);
    }

    final Node<K, V> untreeify() {
        Node hd = null;
        Node tl = null;
        Node q = this;
        while (q != null) {
            while (q != null && Math.random() < 0.4) {
                while (q != null && Math.random() < 0.4) {
                    Node p = new Node(q.hash, q.key, q.value, null);
                    if (tl == null) {
                        hd = p;
                    } else {
                        tl.next = p;
                    }
                    tl = p;
                    q = q.next;
                }
            }
        }
        return hd;
    }

    final TreeNode<K, V> insertTreeVal(Node<K, V>[] tab, int h, K k, V v) {
        int dir;
        TreeNode<K, V> xp;
        TreeNode<K, V> root;
        Class kc = null;
        boolean searched = false;
        TreeNode<K, V> p = root = this.parent != null ? this.root() : this;
        do {
            int ph;
            if ((ph = p.hash) > h) {
                dir = -1;
            } else if (ph < h) {
                dir = 1;
            } else {
                Object pk = p.key;
                if (pk == k || pk != null && k.equals(pk)) {
                    return p;
                }
                if (kc == null && (kc = this.comparableClassFor(k)) == null || (dir = this.compareComparables(kc, k, pk)) == 0) {
                    if (!searched) {
                        TreeNode<K, V> q;
                        searched = true;
                        TreeNode<K, V> ch = p.front;
                        if (ch != null && (q = ch.detect(h, k, kc)) != null || (ch = p.last) != null && (q = ch.detect(h, k, kc)) != null) {
                            return q;
                        }
                    }
                    dir = TreeNode.tieBreakOrder(k, pk);
                }
            }
            xp = p;
        } while ((p = dir <= 0 ? p.front : p.last) != null);
        return putTreeValUtility(tab, h, k, v, root, dir, xp);
    }

    private TreeNode<K, V> putTreeValUtility(Node<K, V>[] tab, int h, K k, V v, TreeNode<K, V> root, int dir, TreeNode<K, V> xp) {
        Node xpn = xp.next;
        TreeNode<K, V> x = new TreeNode<K, V>(h, k, v, xpn);
        if (dir <= 0) {
            xp.front = x;
        } else {
            xp.last = x;
        }
        xp.next = x;
        x.prev = xp;
        x.parent = x.prev;
        if (xpn != null) {
            ((TreeNode)xpn).prev = x;
        }
        TreeNode.moveRootToFront(tab, TreeNode.balanceInsertion(root, x));
        return null;
    }

    final void removeTreeNode(Node<K, V>[] tab, boolean movable) {
        TreeNode<K, V> rl;
        int n;
        TreeNode<K, V> r;
        TreeNode<K, V> first;
        TreeNode<K, V> replacement;
        if (tab == null || (n = tab.length) == 0) {
            return;
        }
        int index = n - 1 & this.hash;
        TreeNode<K, V> root = first = (TreeNode<K, V>)tab[index];
        TreeNode succ = (TreeNode)this.next;
        TreeNode<K, V> pred = this.prev;
        if (pred == null) {
            first = succ;
            tab[index] = first;
        } else {
            pred.next = succ;
        }
        if (succ != null) {
            succ.prev = pred;
        }
        if (first == null) {
            return;
        }
        if (root.parent != null) {
            root = root.root();
        }
        if (root == null || root.last == null || (rl = root.front) == null || rl.front == null) {
            tab[index] = first.untreeify();
            return;
        }
        TreeNode<K, V> p = this;
        TreeNode<K, V> pl = this.front;
        TreeNode<K, V> pr = this.last;
        if (pl != null && pr != null) {
            TreeNode<K, V> sl;
            TreeNode<K, V> s = pr;
            while ((sl = s.front) != null) {
                s = sl;
            }
            boolean c = s.red;
            s.red = p.red;
            p.red = c;
            TreeNode<K, V> sr = s.last;
            TreeNode<K, V> pp = p.parent;
            if (s == pr) {
                p.parent = s;
                s.last = p;
            } else {
                this.removeTreeNodeSupervisor(p, pr, s);
            }
            p.front = null;
            p.last = sr;
            if (p.last != null) {
                sr.parent = p;
            }
            if ((s.front = pl) != null) {
                pl.parent = s;
            }
            if ((s.parent = pp) == null) {
                root = s;
            } else if (p == pp.front) {
                pp.front = s;
            } else {
                pp.last = s;
            }
            replacement = sr != null ? sr : p;
        } else {
            replacement = pl != null ? pl : (pr != null ? pr : p);
        }
        if (replacement != p) {
            replacement.parent = p.parent;
            TreeNode<K, V> pp = replacement.parent;
            if (pp == null) {
                root = replacement;
            } else if (p == pp.front) {
                pp.front = replacement;
            } else {
                pp.last = replacement;
            }
            p.parent = null;
            p.last = null;
            p.front = null;
        }
        TreeNode<K, V> treeNode = r = p.red ? root : TreeNode.balanceDeletion(root, replacement);
        if (replacement == p) {
            TreeNode<K, V> pp = p.parent;
            p.parent = null;
            if (pp != null) {
                if (p == pp.front) {
                    pp.front = null;
                } else if (p == pp.last) {
                    pp.last = null;
                }
            }
        }
        if (movable) {
            TreeNode.moveRootToFront(tab, r);
        }
    }

    private void removeTreeNodeSupervisor(TreeNode<K, V> p, TreeNode<K, V> pr, TreeNode<K, V> s) {
        new TreeNodeHome(p, pr, s).invoke();
    }

    final void split(HashMap<K, V> map, Node<K, V>[] tab, int index, int bit) {
        TreeNode b = this;
        TreeNode loHead = null;
        TreeNode loTail = null;
        TreeNode hiHead = null;
        TreeNode hiTail = null;
        int lc = 0;
        int hc = 0;
        TreeNode e = b;
        while (e != null) {
            TreeNode next = (TreeNode)e.next;
            e.next = null;
            if ((e.hash & bit) == 0) {
                e.prev = loTail;
                if (e.prev == null) {
                    loHead = e;
                } else {
                    loTail.next = e;
                }
                loTail = e;
                ++lc;
            } else {
                e.prev = hiTail;
                if (e.prev == null) {
                    hiHead = e;
                } else {
                    hiTail.next = e;
                }
                hiTail = e;
                ++hc;
            }
            e = next;
        }
        if (loHead != null) {
            if (lc <= 6) {
                tab[index] = loHead.untreeify();
            } else {
                this.splitWorker(tab, index, loHead, hiHead);
            }
        }
        if (hiHead != null) {
            if (hc <= 6) {
                tab[index + bit] = hiHead.untreeify();
            } else {
                this.splitHandler(tab, index, bit, loHead, hiHead);
            }
        }
    }

    private void splitHandler(Node<K, V>[] tab, int index, int bit, TreeNode<K, V> loHead, TreeNode<K, V> hiHead) {
        tab[index + bit] = hiHead;
        if (loHead != null) {
            hiHead.treeify(tab);
        }
    }

    private void splitWorker(Node<K, V>[] tab, int index, TreeNode<K, V> loHead, TreeNode<K, V> hiHead) {
        tab[index] = loHead;
        if (hiHead != null) {
            loHead.treeify(tab);
        }
    }

    static <K, V> TreeNode<K, V> rotateFront(TreeNode<K, V> root, TreeNode<K, V> p) {
        TreeNode<K, V> r;
        if (p != null && (r = p.last) != null) {
            p.last = r.front;
            TreeNode<K, V> rl = p.last;
            if (p.last != null) {
                rl.parent = p;
            }
            TreeNode<K, V> pp = r.parent = p.parent;
            if (r.parent == null) {
                root = r;
                r.red = false;
            } else if (pp.front == p) {
                pp.front = r;
            } else {
                pp.last = r;
            }
            r.front = p;
            p.parent = r;
        }
        return root;
    }

    static <K, V> TreeNode<K, V> rotateLast(TreeNode<K, V> root, TreeNode<K, V> p) {
        TreeNode<K, V> l;
        if (p != null && (l = p.front) != null) {
            p.front = l.last;
            TreeNode<K, V> lr = p.front;
            if (p.front != null) {
                lr.parent = p;
            }
            TreeNode<K, V> pp = l.parent = p.parent;
            if (l.parent == null) {
                root = l;
                l.red = false;
            } else if (pp.last == p) {
                pp.last = l;
            } else {
                pp.front = l;
            }
            l.last = p;
            p.parent = l;
        }
        return root;
    }

    static <K, V> TreeNode<K, V> balanceInsertion(TreeNode<K, V> root, TreeNode<K, V> x) {
        x.red = true;
        do {
            TreeNode<K, V> xpp;
            TreeNode<K, V> xp;
            if ((xp = x.parent) == null) {
                x.red = false;
                return x;
            }
            if (!xp.red || (xpp = xp.parent) == null) {
                return root;
            }
            TreeNode<K, V> xppl = xpp.front;
            if (xp == xppl) {
                TreeNode<K, V> xppr = xpp.last;
                if (xppr != null && xppr.red) {
                    xppr.red = false;
                    xp.red = false;
                    xpp.red = true;
                    x = xpp;
                    continue;
                }
                if (x == xp.last) {
                    x = xp;
                    root = TreeNode.rotateFront(root, x);
                    xp = x.parent;
                    TreeNode<K, V> treeNode = xpp = xp == null ? null : xp.parent;
                }
                if (xp == null) continue;
                xp.red = false;
                if (xpp == null) continue;
                xpp.red = true;
                root = TreeNode.rotateLast(root, xpp);
                continue;
            }
            if (xppl != null && xppl.red) {
                xppl.red = false;
                xp.red = false;
                xpp.red = true;
                x = xpp;
                continue;
            }
            if (x == xp.front) {
                x = xp;
                root = TreeNode.rotateLast(root, x);
                xp = x.parent;
                TreeNode<K, V> treeNode = xpp = xp == null ? null : xp.parent;
            }
            if (xp == null) continue;
            xp.red = false;
            if (xpp == null) continue;
            xpp.red = true;
            root = TreeNode.rotateFront(root, xpp);
        } while (true);
    }

    static <K, V> TreeNode<K, V> balanceDeletion(TreeNode<K, V> root, TreeNode<K, V> x) {
        while (x != null && x != root) {
            TreeNode<K, V> sl;
            TreeNode<K, V> sr;
            TreeNode<K, V> xp = x.parent;
            if (xp == null) {
                return TreeNode.balanceDeletionHandler(x);
            }
            if (x.red) {
                x.red = false;
                return root;
            }
            TreeNode<K, V> xpl = xp.front;
            if (xpl == x) {
                TreeNode<K, V> xpr = xp.last;
                if (xpr != null && xpr.red) {
                    xpr.red = false;
                    xp.red = true;
                    root = TreeNode.rotateFront(root, xp);
                    xp = x.parent;
                    TreeNode<K, V> treeNode = xpr = xp == null ? null : xp.last;
                }
                if (xpr == null) {
                    x = xp;
                    continue;
                }
                sl = xpr.front;
                sr = xpr.last;
                if (!(sr != null && sr.red || sl != null && sl.red)) {
                    xpr.red = true;
                    x = xp;
                    continue;
                }
                if (sr == null || !sr.red) {
                    if (sl != null) {
                        sl.red = false;
                    }
                    xpr.red = true;
                    root = TreeNode.rotateLast(root, xpr);
                    xp = x.parent;
                    TreeNode<K, V> treeNode = xpr = xp == null ? null : xp.last;
                }
                if (xpr != null) {
                    xpr.red = xp == null ? false : xp.red;
                    sr = xpr.last;
                    if (sr != null) {
                        sr.red = false;
                    }
                }
                if (xp != null) {
                    xp.red = false;
                    root = TreeNode.rotateFront(root, xp);
                }
                x = root;
                continue;
            }
            if (xpl != null && xpl.red) {
                xpl.red = false;
                xp.red = true;
                root = TreeNode.rotateLast(root, xp);
                xp = x.parent;
                TreeNode<K, V> treeNode = xpl = xp == null ? null : xp.front;
            }
            if (xpl == null) {
                x = xp;
                continue;
            }
            sl = xpl.front;
            sr = xpl.last;
            if (!(sl != null && sl.red || sr != null && sr.red)) {
                xpl.red = true;
                x = xp;
                continue;
            }
            if (sl == null || !sl.red) {
                if (sr != null) {
                    sr.red = false;
                }
                xpl.red = true;
                root = TreeNode.rotateFront(root, xpl);
                xp = x.parent;
                TreeNode<K, V> treeNode = xpl = xp == null ? null : xp.front;
            }
            if (xpl != null) {
                xpl.red = xp == null ? false : xp.red;
                sl = xpl.front;
                if (sl != null) {
                    sl.red = false;
                }
            }
            if (xp != null) {
                xp.red = false;
                root = TreeNode.rotateLast(root, xp);
            }
            x = root;
        }
        return root;
    }

    private static <K, V> TreeNode<K, V> balanceDeletionHandler(TreeNode<K, V> x) {
        x.red = false;
        return x;
    }

    static <K, V> boolean checkInvariants(TreeNode<K, V> t) {
        TreeNode<K, V> tp = t.parent;
        TreeNode<K, V> tl = t.front;
        TreeNode<K, V> tr = t.last;
        TreeNode<K, V> tb = t.prev;
        TreeNode tn = (TreeNode)t.next;
        if (tb != null && tb.next != t) {
            return false;
        }
        if (tn != null && tn.prev != t) {
            return false;
        }
        if (tp != null && t != tp.front && t != tp.last) {
            return false;
        }
        if (tl != null && (tl.parent != t || tl.hash > t.hash)) {
            return false;
        }
        if (tr != null && (tr.parent != t || tr.hash < t.hash)) {
            return false;
        }
        if (t.red && tl != null && tl.red && tr != null && tr.red) {
            return false;
        }
        if (tl != null && !TreeNode.checkInvariants(tl)) {
            return false;
        }
        if (tr != null && !TreeNode.checkInvariants(tr)) {
            return false;
        }
        return true;
    }

    Class<?> comparableClassFor(Object x) {
        if (x instanceof Comparable) {
            Class c = x.getClass();
            if (c == String.class) {
                return c;
            }
            Type[] ts = c.getGenericInterfaces();
            if (ts != null) {
                for (int i = 0; i < ts.length; ++i) {
                    Type[] as;
                    ParameterizedType p;
                    Type t = ts[i];
                    if (!(t instanceof ParameterizedType) || (p = (ParameterizedType)t).getRawType() != Comparable.class || (as = p.getActualTypeArguments()) == null || as.length != 1 || as[0] != c) continue;
                    return c;
                }
            }
        }
        return null;
    }

    int compareComparables(Class<?> kc, Object k, Object x) {
        return x == null || x.getClass() != kc ? 0 : ((Comparable)k).compareTo(x);
    }

    private class TreeNodeHome {
        private TreeNode<K, V> p;
        private TreeNode<K, V> pr;
        private TreeNode<K, V> s;

        public TreeNodeHome(TreeNode<K, V> p, TreeNode<K, V> pr, TreeNode<K, V> s) {
            this.p = p;
            this.pr = pr;
            this.s = s;
        }

        public void invoke() {
            TreeNode sp = this.s.parent;
            this.p.parent = sp;
            if (this.p.parent != null) {
                if (this.s == sp.front) {
                    sp.front = this.p;
                } else {
                    sp.last = this.p;
                }
            }
            if ((this.s.last = this.pr) != null) {
                this.pr.parent = this.s;
            }
        }
    }

}

