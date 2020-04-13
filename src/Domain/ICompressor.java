package Domain;

public interface ICompressor {
    void calcularFrecuencias();
    void comprimir(byte[] bytes);
    void descomprimir();
}
