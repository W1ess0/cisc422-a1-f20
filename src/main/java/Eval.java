import ast.*;
import ql.QLParser;

import java.util.*;

public class Eval extends BaseEval {
    //-----------------------!! DO NOT MODIFY !!-------------------------
    private int[][] M;
    public Eval(int[][] M) {
        this.M = M;
    }
    //-------------------------------------------------------------------

    @Override
    protected Integer evalNExp(NExp ne, Env env) {
        if (ne instanceof Nat)
            return evalNat((Nat) ne);
        if (ne instanceof SalesAt)
            return evalSalesAt((SalesAt)ne, env);
        if (ne instanceof SalesForP)
            return evalSalesForP((SalesForP)ne, env);
        if (ne instanceof SalesForD)
            return evalSalesForD((SalesForD)ne, env);
        /* I was unable to make the function call work for this
        as it is in sExp format
        if (ne instanceof Size)
            return evalSize((Size)ne, env);

         */
        if (ne instanceof BinaryNExp)
            return evalBinNExp((BinaryNExp) ne, env);

        return 0;

    }

    @Override
    protected Set<Integer> evalSExp(SExp exp, Env env) {
       // if (exp instanceof Type)
         //   return evalDay((Type) exp, env);
        if (exp instanceof BinarySExp)
            return evalBinSExp((BinarySExp)exp, env);
        return null;
    }

    @Override
    protected Boolean evalFormula(Formula f, Env e) {
      if (f instanceof AtomicN) {
          return evalAtomicN((AtomicN)f, e);
      }
      if (f instanceof Binary){
          return evalBinary((Binary)f, e);
      }
      if (f instanceof Unary){
          return evalUnary((Unary)f, e);
      }

        return false;
    }
    public Boolean evalAtomicS(AtomicS f, Env e){
        return true;
    }
    public Boolean evalAtomicN(AtomicN f, Env e) {
        int i = evalNExp(f.lhs, e);
        int r = evalNExp(f.rhs, e);
        if(f.relNOp.kind == RelNOp.LT().kind){
            return (i<r);
        }
        if (f.relNOp.kind == RelNOp.GT().kind){
            return (i>r);
        }
        if (f.relNOp.kind == RelNOp.LTE().kind){
            return (i<=r);
        }
        if (f.relNOp.kind == RelNOp.GTE().kind){
            return (i>=r);
        }
        if (f.relNOp.kind == RelNOp.EQ().kind){
            return (i==r);
        }
        if (f.relNOp.kind == RelNOp.NEQ().kind){
            return (i!=r);
        }
        return null;
    }
    public Integer evalNat(Nat ne){
        return ne.value;
    }



    public boolean evalBinary(Binary f, Env e) {
        boolean i = evalFormula(f.lhs, e);
        boolean r = evalFormula(f.rhs, e);
        boolean c = false;
        if (f.binConn.kind == BinaryConn.AND().kind) {
            return (i && r);
        }
        if (f.binConn.kind == BinaryConn.OR().kind) {
            return (i || r);
        }/* not sure why these would work on my IDE intelli j but I am positive the
        two should work

        if (f.binConn.kind == BinaryConn.IMPLY().kind){
            c = (i => r)
            return c;
        }
        if (f.binConn.kind == BinaryConn.EQUIV().kind){
            return i <==> r;
        }*/
        return false;
    }
    public boolean evalUnary(Unary f, Env e){
        boolean i = evalFormula(f.formula, e);
        if (f.unConn.kind == UnaryConn.NOT().kind){
            return !(i);
        }
        return false;
    }
    public Integer evalSalesAt(SalesAt p, Env e){
        int product = evalNExp(p.product, e)-1;
        int day  = evalNExp(p.day, e)-1;
        int SalesAt = M[product][day];

        return SalesAt;
    }
    public Integer evalSalesForP(SalesForP p, Env e){
        int product  = evalNExp(p.product, e)-1;
        int pSales = 0;
        for (int j = 0; j<M.length; j++){
            pSales = pSales+ M[product][j];
        }
        return pSales;
    }
    public Integer evalSalesForD(SalesForD p, Env e){
        int day  = evalNExp(p.day, e)-1;
        int pSales = 0;
        for (int j = 0; j<M.length; j++){
            pSales = pSales+ M[j][day];}
        return pSales;
    }
    /*
    public Integer evalSize(Size s, Env e){
        return size;
    }*/
    public Integer evalBinNExp(BinaryNExp f, Env e){
        int i = evalNExp(f.lhs, e);
        int r = evalNExp(f.rhs, e);
        if (f.op.kind == BinNOp.ADD().kind)
            return i+r;
        if (f.op.kind == BinNOp.DIFF().kind)
            return i-r;
        if (f.op.kind == BinNOp.MULT().kind)
            return i*r;
        if (f.op.kind == BinNOp.DIV().kind)
            return i/r;
        return 0;
    }
    /*
    public Set<Integer> evalDay(Type t, Env e){
        Set<Integer> a = evalSExp(t.DAY(), e);
        if (a == "Day")
            return a;
        else
            return null;
    }
     */
    public Set<Integer> evalBinSExp(BinarySExp s, Env e){
        Set<Integer> a = evalSExp(s.lhs, e);
        Set<Integer> b = evalSExp(s.rhs, e);

        if (s.op.kind == BinSOp.UNION().kind)
            return a;

        if (s.op.kind == BinSOp.DIFF().kind)
            return a;
        return null;

    }
}
