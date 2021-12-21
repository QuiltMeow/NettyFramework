package ew.sr.x1c.quilt.meow.server;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import lombok.Synchronized;

public final class ClientStorage {

    private final ReentrantReadWriteLock mutex;
    private final Lock readLock;
    private final Lock writeLock;
    private final Map<UUID, Client> uuidClient;

    public ClientStorage() {
        mutex = new ReentrantReadWriteLock();
        readLock = mutex.readLock();
        writeLock = mutex.writeLock();
        uuidClient = new HashMap<>();
    }

    public List<Client> getAllClient() {
        readLock.lock();
        try {
            return new ArrayList<>(uuidClient.values());
        } finally {
            readLock.unlock();
        }
    }

    public void registerClient(Client client) {
        writeLock.lock();
        try {
            uuidClient.put(client.getUUID(), client);
        } finally {
            writeLock.unlock();
        }
    }

    public void deregisterClient(UUID uuid) {
        writeLock.lock();
        try {
            uuidClient.remove(uuid);
        } finally {
            writeLock.unlock();
        }
    }

    public Client getClientByUUID(UUID uuid) {
        readLock.lock();
        try {
            return uuidClient.get(uuid);
        } finally {
            readLock.unlock();
        }
    }

    public void broadcastPacket(byte[] data) {
        readLock.lock();
        try {
            Iterator<Client> iterator = uuidClient.values().iterator();
            Client client;
            while (iterator.hasNext()) {
                client = iterator.next();
                if (client != null) {
                    client.getSession().write(data);
                }
            }
        } finally {
            readLock.unlock();
        }
    }

    public int getOnlineCount() {
        return getAllClient().size();
    }

    @Synchronized
    public void disconnectAll() {
        List<Client> clientList = getAllClient();
        clientList.forEach((client) -> {
            client.disconnect();
            client.getSession().close();
        });
    }
}
