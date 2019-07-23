package dad.app.ts.com.tablayouttest1.present;

import android.content.Context;
import android.os.Bundle;

import dad.app.ts.com.tablayouttest1.model.Register;

public class RegisterPresenter implements IRegisterPresenter, Register.RegisterLocalback {

    private Context mContext;
    private IRegisterView mIRegisterView;
    private Register mRegister;

    public RegisterPresenter(Context mContext, IRegisterView mIRegisterView) {
        this.mContext = mContext;
        this.mIRegisterView = mIRegisterView;
        this.mRegister = new Register();
    }

    @Override
    public void success() {
        mIRegisterView.successRegister();


    }

    @Override
    public void error(String message) {
        mIRegisterView.showMessage(message);


    }

    @Override
    public void connectServer(Bundle bundle) {
        mRegister.register(this, bundle);

    }
}
