# why use GPU for Neural Networks

GPUs definitely can be used to speed up other types of computations besides neural networks.GPU computng is all about leveraging the parallelism in the GPU architecture to perform matcheatically compute intensive operations that the CPU is not designed well to handle.

A cpu is designed for task switching. It is not designed for high throughput. It needs to switch between multiple tasks and store contexts while switching and take care of the shared memory. It is designed for low throughput and high latency jobs.

On the other hand, a GPU is designed for heavy workload and throughput. It is designed for low latency and high throughput jobs. 

Something special about neural networks is that the dot product between the weights and input at every neuron can be run in parallel and the thousands of cores inside a GPU is a perfect candidate for such tasks.

In other words,the reasons for the overperformance of the GPU compared to the CPU: Deep Learning involves a huge amount of matrix and vector operations, mostly multiplications that can be massively parallelized, which implies speed up on GPUs. This is because GPUs were designed to handle these matrix operations in parallel, as this is essential for computer games for e.g. 3D effects, land rendering, etc. On the other hand, a single core CPU would take a matrix operation in serial, one element at a time. A single GPU could have hundreds or thousands of cores, while a CPU typically has no more than a few cores (between two and eight).
