package org.ovirt.engine.ui.webadmin.section.main.presenter.tab.host;

import org.ovirt.engine.core.common.businessentities.VDS;
import org.ovirt.engine.core.common.businessentities.gluster.GlusterBrickEntity;
import org.ovirt.engine.ui.common.presenter.AbstractSubTabPresenter;
import org.ovirt.engine.ui.common.uicommon.model.SearchableDetailModelProvider;
import org.ovirt.engine.ui.uicommonweb.models.hosts.HostBricksListModel;
import org.ovirt.engine.ui.uicommonweb.models.hosts.HostListModel;
import org.ovirt.engine.ui.uicommonweb.place.WebAdminApplicationPlaces;
import org.ovirt.engine.ui.webadmin.section.main.presenter.tab.DetailTabDataIndex;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;

public class SubTabHostBrickPresenter
    extends AbstractSubTabHostPresenter<HostBricksListModel, SubTabHostBrickPresenter.ViewDef,
        SubTabHostBrickPresenter.ProxyDef> {

    @ProxyCodeSplit
    @NameToken(WebAdminApplicationPlaces.hostBricksSubTabPlace)
    public interface ProxyDef extends TabContentProxyPlace<SubTabHostBrickPresenter> {
    }

    public interface ViewDef extends AbstractSubTabPresenter.ViewDef<VDS> {
    }

    @TabInfo(container = HostSubTabPanelPresenter.class)
    static TabData getTabData() {
        return DetailTabDataIndex.HOSTS_BRICKS;
    }

    @Inject
    public SubTabHostBrickPresenter(EventBus eventBus, ViewDef view, ProxyDef proxy,
            PlaceManager placeManager, HostMainTabSelectedItems selectedItems,
            SearchableDetailModelProvider<GlusterBrickEntity, HostListModel<Void>, HostBricksListModel> modelProvider) {
        // View has no action panel.
        super(eventBus, view, proxy, placeManager, modelProvider, selectedItems, null,
                HostSubTabPanelPresenter.TYPE_SetTabContent);
    }

}
