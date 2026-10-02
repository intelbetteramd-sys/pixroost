package app.pixroost.core.spike.phash

/** How many of the 64 bits differ. */
fun hammingDistance(a: Long, b: Long): Int = (a xor b).countOneBits()
