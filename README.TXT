For implementation of business requirements next approach was chosen.
 1) As there where no strict limitations on memory and velocity of Mean.add operation I've decided to add separate double
 variables that counts sum of all elements and product of multiplication of all elements while add operation.
 That gave possibility to get arithmetic and geometric mean lightning fast.
 2) Even though ArrayList is not best collection for adding values (it is copy whole array when size exceed capacity)
 it was chosen as base list because it is fastest collection for access by index, and those operation called much more times
 than adding of elements. Also to to smooth negative impact of that constructor with capacity parameter was created.
 3) For calculation of harmonic and interquartile means also some improvements was made. If the number of elements very big
 (currently it is more that 10k elements) base list split into partitions and sum calculations are made in multiple threads.