package juixPreview.observer;

public interface ISubject {

    public void registerObserver(IObserver observer);

    public void deregisterObserver(IObserver observer);

    public void notifyObservers(IMessage message);

}
