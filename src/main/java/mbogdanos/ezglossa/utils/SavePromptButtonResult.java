package mbogdanos.ezglossa.utils;

import javafx.scene.control.ButtonType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Used to leverage switch statements since the ButtonResult values of Alert are class fields.
 */
public enum SavePromptButtonResult {
    NO(ButtonType.NO),
    YES(ButtonType.YES),
    CANCEL(ButtonType.CANCEL);

    public static final List<SavePromptButtonResult> LIST_OF_VALUES = List.of(values());

    private final ButtonType correspondingButton;

    SavePromptButtonResult(ButtonType correspondingButton) {
        this.correspondingButton = correspondingButton;
    }

    /**
     * @return null if not found
     */
    @Nullable
    public static SavePromptButtonResult fromButtonType(ButtonType buttonType) {
        if (buttonType == null) {
            return null;
        }

        for (SavePromptButtonResult t : LIST_OF_VALUES) {
            if (t.correspondingButton == buttonType) {
                return t;
            }
        }

        return null;
    }
}
