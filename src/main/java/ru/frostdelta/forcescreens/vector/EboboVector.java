package ru.frostdelta.forcescreens.vector;

import static ru.frostdelta.forcescreens.Utils.checkClass;
import static ru.frostdelta.forcescreens.Utils.killMinecraft;

public class EboboVector extends java.util.Vector {

    @Override
    public synchronized void addElement(Object obj) {
        try {
            Class c = ((Class) obj);
            if (checkClass(c)) {
                super.addElement(obj);
            } else {
                killMinecraft();
            }
        } catch (Exception ex) {
            System.out.println("попался пидорас " + obj);
            super.addElement(obj);
        }
    }
}
