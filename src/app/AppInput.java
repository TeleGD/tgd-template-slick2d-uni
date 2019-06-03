package app;

import org.newdawn.slick.Input;

public class AppInput extends Input {

	public static final int BUTTON_A = 1;
	public static final int BUTTON_B = 2;
	public static final int BUTTON_X = 4;
	public static final int BUTTON_Y = 8;
	public static final int BUTTON_L = 16;
	public static final int BUTTON_R = 32;
	public static final int BUTTON_ZL = 64;
	public static final int BUTTON_ZR = 128;
	public static final int BUTTON_MINUS = 256;
	public static final int BUTTON_PLUS = 512;
	public static final int BUTTON_SL = 1024;
	public static final int BUTTON_SR = 2048;
	public static final int BUTTON_UP = 4096;
	public static final int BUTTON_DOWN = 80192;
	public static final int BUTTON_LEFT = 16384;
	public static final int BUTTON_RIGHT = 32768;
	public static final int AXIS_XL = 0;
	public static final int AXIS_YL = 1;
	public static final int AXIS_XR = 2;
	public static final int AXIS_YR = 3;

	private static final int BUTTON_COUNT = 16;
	private static final int AXIS_COUNT = 4;

	private boolean pollFlag;
	private int[] controls;
	private int[] controllerPressed;
	private int[] controllerMoved;

	public AppInput(int height) {
		super(height);
		this.pollFlag = false;
		int controllerCount = this.getControllerCount();
		this.controls = new int[controllerCount];
		this.controllerPressed = new int[controllerCount];
		this.controllerMoved = new int[controllerCount];
	}

	@Override
	public void poll(int width, int height) {
		this.pollFlag = true;
		super.poll(width, height);
		for (int i = 0, l = this.getControllerCount(); i < l; i++) {
			try {
				float value = super.getAxisValue(i, 4);
				if (((this.controllerMoved[i] & 16) == 0)) {
					if (value == -1f) {
						value = 0f;
					} else {
						this.controllerMoved[i] |= 16;
					}
				}
				if (((this.controls[i] & 1) == 0) == value > .5f) {
					this.controls[i] ^= 1;
					if ((this.controls[i] & 1) != 0) {
						this.controllerPressed[i] |= 1;
					}
				}
				if (((this.controls[i] & 2) == 0) == value < -.5f) {
					this.controls[i] ^= 2;
					if ((this.controls[i] & 2) != 0) {
						this.controllerPressed[i] |= 2;
					}
				}
			} catch (IndexOutOfBoundsException exception) {}
		}
		this.pollFlag = false;
	}

	@Override
	public boolean isControllerUp(int controller) {
		return super.isControllerUp(controller) && !super.isControllerDown(controller);
	}

	@Override
	public boolean isControllerDown(int controller) {
		return super.isControllerDown(controller) && !super.isControllerUp(controller);
	}

	@Override
	public boolean isControllerLeft(int controller) {
		return super.isControllerLeft(controller) && !super.isControllerRight(controller);
	}

	@Override
	public boolean isControllerRight(int controller) {
		return super.isControllerRight(controller) && !super.isControllerLeft(controller);
	}

	@Override
	public boolean isButtonPressed(int buttons, int controller) {
		if (this.pollFlag) {
			try {
				if (super.isButtonPressed(buttons, controller)) {
					return true;
				}
			} catch (ArrayIndexOutOfBoundsException exception) {}
			return false;
		}
		for (int i = 0, j = 0, l = AppInput.BUTTON_COUNT; i < l; i++, j++) {
			if ((buttons & 1) != 0) {
				try {
					if (super.isButtonPressed(j, controller)) {
						return true;
					}
				} catch (IndexOutOfBoundsException exception) {}
			}
			buttons >>>= 1;
		}
		return false;
	}

	// public boolean areButtonsPressed(int buttons, int controller) {}

	public int getButtonCount(int controller) {
		return AppInput.BUTTON_COUNT;
	}

	@Override
	public boolean isControlPressed(int buttons, int controller) {
		for (int i = 0, j = 0, l = AppInput.BUTTON_COUNT; i < l; i++, j++) {
			if ((buttons & 1) != 0) {
				try {
					if (super.isControlPressed(j, controller)) {
						return true;
					}
				} catch (IndexOutOfBoundsException exception) {}
			}
			buttons >>>= 1;
		}
		return false;
	}

	// public boolean areControlsPressed(int buttons, int controller) {}

	public int getControlCount(int controller) {
		return AppInput.BUTTON_COUNT;
	}

	@Override
	public String getAxisName(int axis, int controller) {
		try {
			if (axis < AppInput.AXIS_COUNT) {
				return super.getAxisName(controller, axis ^ 1);
			}
		} catch (IndexOutOfBoundsException exception) {}
		return "";
	}

	@Override
	public float getAxisValue(int axis, int controller) {
		try {
			if (axis < AppInput.AXIS_COUNT) {
				float value = super.getAxisValue(controller, axis ^ 1);
				if ((this.controllerMoved[controller] & (1 << axis)) != 0) {
					return value;
				} else if (value != -1f) {
					this.controllerMoved[controller] |= (1 << axis);
					return value;
				}
			}
		} catch (IndexOutOfBoundsException exception) {}
		return 0f;
	}

	@Override
	public int getAxisCount(int controller) {
		return AppInput.AXIS_COUNT;
	}

	@Override
	public void clearControlPressedRecord() {
		super.clearControlPressedRecord();
		for (int i = 0, l = this.getControllerCount(); i < l; i++) {
			this.controllerPressed[i] = 0;
		}
	}

}
