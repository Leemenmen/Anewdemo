import numpy as np

import pandas as pd

data = pd.DataFrame(np.arange(9).reshape(3, 3))
data.to_csv("data_ANSI.csv",encoding="ANSI")