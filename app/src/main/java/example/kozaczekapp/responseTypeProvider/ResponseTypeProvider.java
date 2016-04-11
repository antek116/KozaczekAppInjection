package example.kozaczekapp.responseTypeProvider;

import javax.inject.Inject;

public class ResponseTypeProvider {
    private IResponseType provider;
    private static ResponseTypeProvider instance = null;

    private ResponseTypeProvider(IResponseType provider) {
        this.provider = provider;
    }


    @Inject
    public static ResponseTypeProvider getInstance(IResponseType provider){
        if(instance == null){
            instance = new ResponseTypeProvider(provider);
        }else{
            instance.provider = provider;
        }
        return instance;
    }
}
