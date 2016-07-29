package com.example.srikar.magic.model.zone;

import com.example.srikar.magic.MagicLog;
import com.example.srikar.magic.event.GameStateChangeEvent;
import com.example.srikar.magic.event.ListChangeEvent;
import com.example.srikar.magic.event.RxEventBus;
import com.example.srikar.magic.model.DataModelConstants;
import com.example.srikar.magic.model.zone.BaseGameZone;

import org.junit.Test;

import rx.Subscription;
import rx.schedulers.Schedulers;

import static org.junit.Assert.assertTrue;

/**
 * Testing code shared by Battlefield and Hand in BaseGameZone.
 * Created by Srikar on 7/20/2016.
 */
public class BaseGameZoneUnitTest {
    private final RxEventBus<ListChangeEvent> eventBus;
    private final BaseGameZone baseGameZone;

    public BaseGameZoneUnitTest() {
        MagicLog.setLogging(false);

        eventBus = new RxEventBus<>();
        //create implementation that can test
        baseGameZone = new BaseGameZone(eventBus, null, new RxEventBus<>()) {
            @Override
            protected void clearLists() {
                //there are no lists to clear
            }

            @Override
            protected void actOnGameStateChangeEvent(GameStateChangeEvent event) {
                //not testing response to changes in GameState
            }
        };
    }

    /**
     * Test for addListChangeEvent()
     */
    @Test
    public void testAddListChangeEvent() {
        //listen for events
        Subscription sub = eventBus.getEvents()
                .observeOn(Schedulers.newThread())
                .subscribe(this::listenChangeEventListener);

        baseGameZone.addListChangeEvent(
                DataModelConstants.LIST_HAND,
                ListChangeEvent.UPDATE_ALL,
                0
        );

        //wait for response
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            sub.unsubscribe();
            assertTrue("Thread failed", false);
        }

        assertTrue("Got wrong event " + ((receivedEvent == null)? "null" : receivedEvent.toString()),
                isEventReceived);
    }

    private boolean isEventReceived = false;
    private ListChangeEvent receivedEvent = null;

    /**
     * Used by testAddListChangeEvent() to test if event received.
     * @param event Event received
     */
    private void listenChangeEventListener(ListChangeEvent event) {
        receivedEvent = event;
        isEventReceived = (event.listName == DataModelConstants.LIST_HAND)
                && (event.action == ListChangeEvent.UPDATE_ALL)
                && (event.index == 0);
    }
}
