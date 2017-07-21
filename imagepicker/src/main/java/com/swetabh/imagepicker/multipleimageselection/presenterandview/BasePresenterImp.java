package com.swetabh.imagepicker.multipleimageselection.presenterandview;

/**
 * Created by "swets" on 07/20/2017.
 */

public class BasePresenterImp<T extends BaseView> implements BasePresenter {
    public T mView;

    @Override
    public void start() {
        if (mView != null) {
            mView.updateActionBar();
        }
    }

    @Override
    public void attachView(BaseView view) {
        this.mView = (T) view;
        if (mView != null)
            mView.setPresenter(this);
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    @Override
    public void onAddClicked() {

    }


}
