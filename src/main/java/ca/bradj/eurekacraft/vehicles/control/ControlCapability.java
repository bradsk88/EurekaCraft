package ca.bradj.eurekacraft.vehicles.control;

public class ControlCapability {
    public static final ControlCapability NONE = new ControlCapability(Control.NONE);
    private Control control;

    public ControlCapability(Control c) {
        this.control = c;
    }

    public boolean setControl(Control c) {
        if (control != null && control.equals(c)) {
            return false;
        }
        control = c;
        return true;
    }

    public Control getControl() {
        return control;
    }
}
