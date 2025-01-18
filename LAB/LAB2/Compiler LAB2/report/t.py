import matplotlib.pyplot as plt
import numpy as np

# 线程数
workers = np.array([1, 2, 4, 8, 16])

# 数组规模 (虽然这里未直接使用，但为理解数据结构保留)
sizes = np.array([1, 2, 4, 8, 16, 32, 64, 128])

# 划分1的数据
data1 = np.array([
    [.001481, .002983, .005647, .011060, .021771, .044333, .088551, .178929],
    [.000806, .001530, .002885, .005695, .011572, .023171, .047254, .092074],
    [.000623, .000949, .001713, .004073, .007763, .014384, .023820, .045619],
    [.000762, .001147, .001613, .003309, .006813, .013369, .013515, .041619],
    [.001367, .001779, .002284, .004367, .005254, .011448, .018628, .033708]
])

# 划分2的数据
data2 = np.array([
    [.001505, .002958, .005740, .011594, .022237, .043872, .085593, .173632],
    [.000893, .001481, .002779, .005510, .011183, .022117, .044044, .092859],
    [.000681, .001270, .002966, .002889, .005716, .011321, .023251, .052073],
    [.000629, .001289, .001648, .002874, .004138, .009129, .019201, .035495],
    [.000517, .000816, .001202, .002754, .004697, .008007, .015590, .026738]
])

# 创建图表
plt.figure(figsize=(12, 8))

# 使用不同的颜色来代表不同的数组规模
colors = plt.cm.viridis(np.linspace(0, 1, len(sizes)))

for i, size in enumerate(sizes):
    # 划分1数据
    plt.plot(workers, data1[:, i], 'o-', color=colors[i],
             label=f'Partition 1 - Size {size}M')
    # 划分2数据
    plt.plot(workers, data2[:, i], 'x--', color=colors[i],
             label=f'Partition 2 - Size {size}M')

plt.title('Performance Data: Thread Count vs Array Size')
plt.xlabel('Number of Worker Processes')
plt.ylabel('Time (Seconds)')
plt.xscale('log', base=2)  # Logarithmic scale for x-axis
plt.yscale('log')          # Logarithic scale for y-axis
plt.legend(bbox_to_anchor=(1.05, 1), loc='upper left')
plt.grid(True)
plt.tight_layout(rect=[0, 0, 0.75, 1])  # Adjust layout to make room for legend
plt.show()
