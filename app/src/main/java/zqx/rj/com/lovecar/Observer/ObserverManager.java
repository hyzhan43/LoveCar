package zqx.rj.com.lovecar.Observer;

/**
 * author：  HyZhan
 * created：2018/9/20 13:58
 * desc：    TODO
 */

public class ObserverManager {

    private UpdateNewTicketsListener listener;

    public static class Holder{
        public static final ObserverManager instance = new ObserverManager();
    }

    public void register(UpdateNewTicketsListener listener){
        this.listener = listener;
    }

    public void notifyListener(){
        listener.refresh();
    }
}
