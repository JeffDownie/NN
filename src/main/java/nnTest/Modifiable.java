package nnTest;

public interface Modifiable<M extends Modifiable<M, D>, D extends Delta<M, D>> {
  M applyDelta(D delta);
  D createRandomDelta();
}
