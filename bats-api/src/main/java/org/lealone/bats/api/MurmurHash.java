package org.lealone.bats.api;

/**
 * This is a very fast, non-cryptographic hash suitable for general hash-based
 * lookup. See http://murmurhash.googlepages.com/ for more details.
 *
 * <p>The C version of MurmurHash 2.0 found at that site was ported
 * to Java by Andrzej Bialecki (ab at getopt org).</p>
 *
 * <p>Customized for DataTorrent's use for hash of byte array slice by
 * Chetan Narsude (chetan at datatorrent com).</p>
 *
 * @since 1.0.0
 */
// org.getopt.util.hash.MurmurHash
class MurmurHash {
    public static int hash(byte[] data, int seed) {
        return hash(data, seed, 0, data.length);
    }

    public static int hash(byte[] data, int seed, int offset, int length) {
        int m = 0x5bd1e995;
        int r = 24;

        int h = seed ^ length;

        int len_4 = length >> 2;

        for (int i = 0; i < len_4; i++) {
            int i_4 = offset + (i << 2);
            int k = data[i_4 + 3];
            k <<= 8;
            k |= (data[i_4 + 2] & 0xff);
            k <<= 8;
            k |= (data[i_4 + 1] & 0xff);
            k <<= 8;
            k |= (data[i_4 + 0] & 0xff);
            k *= m;
            k ^= k >>> r;
            k *= m;
            h *= m;
            h ^= k;
        }

        int len_m = len_4 << 2;
        int left = length - len_m;

        if (left != 0) {
            length += offset;
            if (left >= 3) {
                h ^= data[length - 3] << 16;
            }
            if (left >= 2) {
                h ^= data[length - 2] << 8;
            }
            if (left >= 1) {
                h ^= data[length - 1];
            }

            h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

}
