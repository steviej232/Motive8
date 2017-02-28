package sjohns70.motive8.data;

/**
 * Created by KendallGassner on 2/23/17.
 */

public class BusinessPagerData {

        private int mTitleResId;
        private int mLayoutResId;

        BusinessPagerData(int titleResId, int layoutResId) {
            mTitleResId = titleResId;
            mLayoutResId = layoutResId;
        }

        public int getTitleResId() {
            return mTitleResId;
        }

        public int getLayoutResId() {
            return mLayoutResId;
        }

    }
