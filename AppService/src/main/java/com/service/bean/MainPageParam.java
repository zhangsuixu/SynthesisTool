package com.service.bean;

import com.service.base.BaseBean;

import java.util.List;

/**
 * 时间 :  2019/11/23;
 * 版本号 :
 */
public class MainPageParam extends BaseBean<MainPageParam> {

    public List<MainBottomParam> mainBottoms;
    public List<MainFragmentParam> mainFragmentParams;

    public class MainFragmentParam{
        public int fragmentsId;

        public MainFragmentParam(int fragmentsId) {
            this.fragmentsId = fragmentsId;
        }
    }

    public class MainBottomParam{
        public String name;
        public int selectedId;
        public int unselectedId;
        public boolean isSelected;

        public MainBottomParam(String name, int selectedId, int unselectedId, boolean isSelected) {
            this.name = name;
            this.selectedId = selectedId;
            this.unselectedId = unselectedId;
            this.isSelected = isSelected;
        }
    }

}
