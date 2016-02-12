package nnTest.network.api;

public interface Modifiable<M extends Modifiable<M, D>, D extends Delta<M, D>> {
  M applyDelta(D delta);
}
