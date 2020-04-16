package Domain.Interficies;

import Domain.Node;

import java.util.Map;

public interface ICompressor extends Runnable{
    void comprimir();
    void encode(Node root, String str, Map<Byte, String> huffmanCode);
    int decode(Node root, int index, StringBuilder sb);
}
