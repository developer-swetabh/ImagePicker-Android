package com.swetabh.imagepicker.multipleimageselection.presenterandview;

/**
 * Created by "Mahendran Sakkarai" on 2/14/2017.
 */

public interface BasePresenter {
    void start();

    void attachView(BaseView view);

    void detachView();

    void onAddClicked();

}
