import { type DependencyList, type FC, type ReactNode, useMemo } from 'react';
import type { EmptyObject } from 'type-fest';

export type FetchCallback<T extends ReactNode> = () => Promise<T>;

export function load<T extends ReactNode>(callback: FetchCallback<T>): FC<EmptyObject> {
  let data: T | undefined;
  let promise = callback().then((d) => {
    data = d;
  });

  return () => {
    if (!data) {
      throw promise;
    }

    return data;
  };
}

export function useLoad(callback: FetchCallback<ReactNode>, deps: DependencyList): FC<EmptyObject> {
  return useMemo(() => load(callback), deps);
}
