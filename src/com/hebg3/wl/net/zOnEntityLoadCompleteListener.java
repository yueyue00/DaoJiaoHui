package com.hebg3.wl.net;

public interface zOnEntityLoadCompleteListener<T> extends OnErrorListener {
	void onEntityLoadComplete(Object base);

	void onError(T entity);
}
