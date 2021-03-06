/*
 * Copyright 2017 dmfs GmbH
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dmfs.android.colorpicker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import org.dmfs.android.colorpicker.ColorPickerDialogFragment.ColorDialogResultListener;
import org.dmfs.android.colorpicker.palettes.ArrayPalette;
import org.dmfs.android.colorpicker.palettes.ColorFactory;
import org.dmfs.android.colorpicker.palettes.ColorShadeFactory;
import org.dmfs.android.colorpicker.palettes.CombinedColorFactory;
import org.dmfs.android.colorpicker.palettes.FactoryPalette;
import org.dmfs.android.colorpicker.palettes.Palette;
import org.dmfs.android.colorpicker.palettes.RainbowColorFactory;
import org.dmfs.android.retentionmagic.FragmentActivity;
import org.dmfs.android.retentionmagic.annotations.Retain;


/**
 * An activity that prompts the user to pick a color.
 * <p>
 * To use it include the following XML fragment in your <code>AndroidManifest.xml</code>
 * <p>
 * <pre>
 *        &lt;activity android:name="org.dmfs.android.colorpicker.ColorPickerActivity" >
 *             &lt;intent-filter>
 *                 &lt;action android:name="org.openintents.action.PICK_COLOR" />
 *
 *                 &lt;category android:name="android.intent.category.DEFAULT" />
 *             &lt;/intent-filter>
 *         &lt;/activity>
 * </pre>
 *
 * @author Marten Gajda
 */
public final class ColorPickerActivity extends FragmentActivity implements ColorDialogResultListener
{
    /**
     * Color picker action.
     */
    public final static String ACTION_PICK_COLOR = "org.openintents.action.PICK_COLOR";

    /**
     * The extra that contains the picked color.
     */
    public final static String EXTRA_COLOR = "org.openintents.extra.COLOR";

    private final static int[] MATERIAL_COLORS_PRIMARY = {
            0xffe91e63, 0xfff44336, 0xffff5722, 0xffff9800, 0xffffc107, 0xffffeb3b, 0xffcddc39, 0xff8bc34a,
            0xff4caf50, 0xff009688, 0xff00bcd4, 0xff03a9f4, 0xff2196f3, 0xff3f51b5, 0xff673ab7, 0xff9c27b0 };

    private static final int MATERIAL_COLORS_DARK[] = {
            0xffad1457, 0xffc62828, 0xffd84315, 0xffef6c00, 0xffff8f00, 0xfff9a825, 0xff9e9d24, 0xff558b2f,
            0xff2e7d32, 0xff00695c, 0xff00838f, 0xff0277bd, 0xff1565c0, 0xff283593, 0xff4527a0, 0xff6a1b9a };
    /**
     * The palettes to show.
     */
    private final static Palette[] PALETTES = {
            new ArrayPalette("material_primary", "Material Colors", MATERIAL_COLORS_PRIMARY),
            new ArrayPalette("material_secondary", "Dark Material Colors", MATERIAL_COLORS_DARK),
            new FactoryPalette("red", "Red", new CombinedColorFactory(new ColorShadeFactory(340), ColorFactory.RED), 16),
            new FactoryPalette("orange", "Orange", new CombinedColorFactory(new ColorShadeFactory(18), ColorFactory.ORANGE), 16),
            new FactoryPalette("yellow", "Yellow", new CombinedColorFactory(new ColorShadeFactory(53), ColorFactory.YELLOW), 16),
            new FactoryPalette("green", "Green", new CombinedColorFactory(new ColorShadeFactory(80), ColorFactory.GREEN), 16),
            new FactoryPalette("cyan", "Cyan", new CombinedColorFactory(new ColorShadeFactory(150), ColorFactory.CYAN), 16),
            new FactoryPalette("blue", "Blue", new CombinedColorFactory(new ColorShadeFactory(210), ColorFactory.BLUE), 16),
            new FactoryPalette("purple", "Purple", new CombinedColorFactory(new ColorShadeFactory(265), ColorFactory.PURPLE), 16),
            new FactoryPalette("pink", "Pink", new CombinedColorFactory(new ColorShadeFactory(300), ColorFactory.PINK), 16),
            new FactoryPalette("grey", "Grey", ColorFactory.GREY, 16), new FactoryPalette("pastel", "Pastel", ColorFactory.PASTEL, 16),
            new FactoryPalette("rainbow", "Rainbow", ColorFactory.RAINBOW, 16),
            new FactoryPalette("dark_rainbow", "Dark Rainbow", new RainbowColorFactory(0.5f, 0.5f), 16) };

    /**
     * The id of the palette
     */
    @Retain(classNS = "ColorPickerActivity", key = "palette", permanent = true)
    private String mPaletteId = null;


    /**
     * Start the {@link ColorPickerActivity} with the given request code.
     *
     * @param context
     *         A {@link Context}.
     * @param requestCode
     *         The request code.
     */
    public static void start(android.app.Activity context, int requestCode)
    {
        Intent intent = new Intent(ACTION_PICK_COLOR);
        context.startActivityForResult(intent, requestCode);
    }


    /**
     * Returns the color from the given result intent or <code>null</code> if there was no color.
     *
     * @param result
     *         The intent to get the color from.
     *
     * @return the color or <code>null</code>.
     */
    public static Integer getColor(Intent result)
    {
        return result != null && result.hasExtra(EXTRA_COLOR) ? result.getIntExtra(EXTRA_COLOR, 0) : null;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        showColorPickerDialog();

    }


    private void showColorPickerDialog()
    {
        ColorPickerDialogFragment d = new ColorPickerDialogFragment();
        d.setPalettes(PALETTES);
        d.setTitle(R.string.org_dmfs_colorpicker_pick_a_color);
        d.selectPaletteId(mPaletteId);
        d.show(getSupportFragmentManager(), "");
    }


    @Override
    public void onColorChanged(int color, String paletteId, String colorName, String paletteName)
    {
        mPaletteId = paletteId;
        Intent intent = getIntent();
        intent.putExtra(EXTRA_COLOR, color);
        setResult(RESULT_OK, intent);
        finish();
    }


    @Override
    public void onColorDialogCancelled()
    {
        setResult(RESULT_CANCELED);
        finish();
    }
}
