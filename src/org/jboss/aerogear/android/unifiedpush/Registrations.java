/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.android.unifiedpush;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jboss.aerogear.android.impl.unifiedpush.DefaultPushRegistrarFactory;

/**
 * This is the factory and accessors for PushRegistrars
 */
public class Registrations {
    
    private final PushRegistrarFactory factory;
    private final Map<String, PushRegistrar> registrars = new HashMap<String, PushRegistrar>();

    private static List<MessageHandler> mainThreadHandlers = new ArrayList<MessageHandler>();
    private static List<MessageHandler> backgroundThreadHandlers = new ArrayList<MessageHandler>();
    
    public Registrations() {
        this.factory = new DefaultPushRegistrarFactory();
    }

    public Registrations(PushRegistrarFactory factory) {
        this.factory = factory;
    }
    
    /**
     * @param name the name which will be used to look up the registrar later
     * @param config
     * @return 
     * 
     * @throws  IllegalArgumentException is config.type is not a supported type
     */
    public PushRegistrar push(String name, PushConfig config) {
        PushRegistrar registrar = factory.createPushRegistrar(config);
        registrars.put(name, registrar);
        return registrar;
    }
    
    public PushRegistrar get(String name) {
        return registrars.get(name);
    }
    
    public static void registerMainThreadHandler(MessageHandler handler) {
        mainThreadHandlers.add(handler);
    }

    public static void registerBackgroundThreadHandler(MessageHandler handler) {
        backgroundThreadHandlers.add(handler);
    }

    public static void unregisterMainThreadHandler(MessageHandler handler) {
        mainThreadHandlers.remove(handler);
    }

    public static void unregisterBackgroundThreadHandler(MessageHandler handler) {
        backgroundThreadHandlers.remove(handler);
    }

    public static void notifyHandlers(final Context context, final Intent message, final MessageHandler defaultHandler) {

        if (backgroundThreadHandlers.isEmpty() && mainThreadHandlers.isEmpty()) {
            new Thread(new Runnable() {
                public void run() {

                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                    String messageType = gcm.getMessageType(message);
                    if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                        defaultHandler.onError();
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                        defaultHandler.onDeleteMessage(context, message.getExtras());
                    } else {
                        defaultHandler.onMessage(context, message.getExtras());
                    }

                }
            }).start();
        }

        for (final MessageHandler handler : backgroundThreadHandlers) {
            new Thread(new Runnable() {
                public void run() {

                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                    String messageType = gcm.getMessageType(message);
                    if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                        handler.onError();
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                        handler.onDeleteMessage(context, message.getExtras());
                    } else {
                        handler.onMessage(context, message.getExtras());
                    }

                }
            }).start();
        }

        Looper main = Looper.getMainLooper();

        for (final MessageHandler handler : mainThreadHandlers) {
            new Handler(main).post(new Runnable() {
                @Override
                public void run() {
                    GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
                    String messageType = gcm.getMessageType(message);
                    if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                        handler.onError();
                    } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                        handler.onDeleteMessage(context, message.getExtras());
                    } else {
                        handler.onMessage(context, message.getExtras());
                    }
                }
            });
        }
    }

    protected static void notifyHandlers(final Context context,
            final Intent message) {
        notifyHandlers(context, message, null);
    }
    
}
