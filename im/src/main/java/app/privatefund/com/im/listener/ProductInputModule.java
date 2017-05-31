package app.privatefund.com.im.listener;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.model.Conversation;

/**
 * @author chenlong
 *
 */
public class ProductInputModule extends DefaultExtensionModule {

    private Context context;
    private ProductInputPlugin myPlugin;

    public ProductInputModule(Context context) {
        this.context = context;
    }

    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules =  new ArrayList<>();
        if (myPlugin == null) {
            myPlugin = new ProductInputPlugin(context);
        }
        pluginModules.add(myPlugin);
        return pluginModules;
    }
}
