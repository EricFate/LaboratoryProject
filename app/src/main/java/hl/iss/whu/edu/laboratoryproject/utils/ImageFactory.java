package hl.iss.whu.edu.laboratoryproject.utils;

import hl.iss.whu.edu.laboratoryproject.R;

/**
 * Created by fate on 2016/12/11.
 */

public class ImageFactory {
    public static int getId(int position) {
        switch (position) {
            case 0:
                return R.drawable.image;
            case 1:
                return R.drawable.image2;
            case 2:
                return R.drawable.image1;
            default:
                return 0;
        }
    }
}
