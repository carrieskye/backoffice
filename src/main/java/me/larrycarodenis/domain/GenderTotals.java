package me.larrycarodenis.domain;

public class GenderTotals
{

    private int M;
    private int F;

    public GenderTotals(int M, int F)
    {
        setM(M);
        setF(F);
    }

    public void setM(int M)
    {
        this.M = M;
    }

    public void setF(int F)
    {
        this.F = F;
    }

    public int getM()
    {
        return M;
    }

    public int getF()
    {
        return F;
    }







}
