package nnTest.network.api;

public interface RandomModifiable<M extends RandomModifiable<M, D>, D extends Delta<M, D>> extends Modifiable<M, D>{
  D createRandomDelta();
}
