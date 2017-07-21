package com.swetabh.imagepicker.multipleimageselection.presenterandview;

/**
 * Created by "swets" on 07/20/2017.
 */

public interface BaseView<T, U> {
    void setPresenter(T presenter);

    void setCommunicator(U communicator);

    void updateActionBar();

}
