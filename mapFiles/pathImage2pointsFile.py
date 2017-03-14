from skimage import data, io, transform
import time


def process(filename):
    img = data.imread(filename + '.png')
    f = open(filename + '.txt', 'w')
    for x in range(len(img)):
        for y in range(len(img[x])):
            if img[x][y] == 0:
                f.write(str(y) + ',' + str(x) + '\n')

process('Concordia-Floor-Map-H4')
process('Concordia-Floor-Map-H2')
process('Concordia-Floor-Map-H1')
