package nnTest.network.api;

public interface Delta<M extends Modifiable<M, D>, D extends Delta<M, D>> {
  D scale(double scalar);
  D add(D addTo);
}