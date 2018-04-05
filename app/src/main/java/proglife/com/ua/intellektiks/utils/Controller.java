package proglife.com.ua.intellektiks.utils;

import android.content.Context;
import android.view.View;

import ru.yandex.yandexmapkit.MapView;
import ru.yandex.yandexmapkit.OverlayManager;
import ru.yandex.yandexmapkit.map.OnMapListener;
import ru.yandex.yandexmapkit.overlay.balloon.BalloonItem;
import ru.yandex.yandexmapkit.utils.GeoPoint;
import ru.yandex.yandexmapkit.utils.Point;
import ru.yandex.yandexmapkit.utils.ScreenPoint;

public class Controller {
    private final ru.yandex.yandexmapkit.MapController mc;
    private Context context;

    public Controller(MapView map) {
        mc = map.getMapController();
        context = map.getContext();
    }

    public Context getContext() {
        return context;
    }

    public void setHDMode(boolean hdMode) {
        mc.setHDMode(hdMode);
    }

    public void setZoomCurrent(float zoom) {
        mc.setZoomCurrent(zoom);
    }

    public float getZoomCurrent() {
        return mc.getZoomCurrent();
    }

    public void zoomIn() {
        mc.zoomIn();
    }

    public void zoomOut() {
        mc.zoomOut();
    }

    public ScreenPoint getScreenPoint(Point point) {
        return mc.getScreenPoint(point);
    }

    public Point get23Point(ScreenPoint point) {
        return mc.get23Point(point);
    }

    public View getMapView() {
        return mc.getMapView();
    }

    public OverlayManager getOverlayManager() {
        return mc.getOverlayManager();
    }

    public void showBalloon(BalloonItem balloonItem) {
        mc.showBalloon(balloonItem);
    }

    public void notifyRepaint() {
        mc.notifyRepaint();
    }

    public void setPositionNoAnimationTo(GeoPoint point, float zoom) {
        mc.setPositionNoAnimationTo(point, zoom);
    }

    public void setPositionAnimationTo(GeoPoint point, float zoom) {
        mc.setPositionAnimationTo(point, zoom);
    }

    public void addMapListener(OnMapListener onMapListener) {
        mc.addMapListener(onMapListener);
    }

    public void removeMapListener(OnMapListener onMapListener) {
        mc.removeMapListener(onMapListener);
    }

    public int getWidth() {
        return mc.getWidth();
    }

    public int getHeight() {
        return mc.getHeight();
    }

    public GeoPoint getGeoPoint(ScreenPoint screenPoint) {
        return mc.getGeoPoint(screenPoint);
    }
}