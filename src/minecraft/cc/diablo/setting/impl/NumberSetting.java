package cc.diablo.setting.impl;

import cc.diablo.setting.Setting;
import lombok.Getter;
import lombok.Setter;

@Getter@Setter
public class NumberSetting extends Setting {
    public double val, min, max, inc,def;
    public float value;

    public NumberSetting(String name, double val, double min, double max, double inc)
    {
        this.name = name;
        this.def = val;
        this.val = val;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.value = (float) val;
    }

    public double getVal()
    {
        return val;
    }

    public float getValue()
    {
        return value;
    }

    public double getDefault()
    {
        return def;
    }

    public void increment(boolean pos)
    {
        setValue(getVal() + (pos ? 1 : -1) * inc);
    }

    public void setValue(double val)
    {
        double pres = 1 / inc;
        this.val = Math.round(Math.max(getMin(), Math.min(getMax(), val)) * pres) / pres;
        this.value = (float) this.val;
    }
}
