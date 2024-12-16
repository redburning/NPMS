import pandas as pd
from rdkit import Chem
from rdkit.Chem import Draw
import os
import re

# 读取Excel文件
input_file = 'E://cdc-workspace/herbomics/src/test/resources/dataset.xlsx'
output_folder = 'output_svgs'

# 创建输出文件夹（如果不存在）
if not os.path.exists(output_folder):
    os.makedirs(output_folder)

# 读取Excel文件中的数据
df = pd.read_excel(input_file)

# 确保SMILES列中的值是字符串类型
df['SMILES'] = df['SMILES'].astype(str)

# 检查是否有SMILES列和IPT列
if 'SMILES' not in df.columns:
    raise ValueError("Excel文件中没有找到SMILES列。")
if 'IPT' not in df.columns:
    raise ValueError("Excel文件中没有找到IPT列。")

# 定义一个函数将SMILES转换为SVG并保存到磁盘
def smiles_to_svg(smiles, ipt):
    # 检查是否为空值或无效字符串
    if pd.isna(smiles) or smiles == 'nan' or smiles.strip() == '':
        return None
    
    # 尝试将SMILES转换为分子对象
    molecule = Chem.MolFromSmiles(smiles)
    if molecule is None:
        return None
    
    # 将分子对象转换为SVG
    svg_string = Draw.MolToFile(molecule, temp_file := 'temp.svg', isSVG=True)
    with open(temp_file, 'r') as file:
        svg_content = file.read()
    
    # 将SVG内容中的fill: #FFFFFF; 改为 fill: transparent;
    svg_content = re.sub(r'fill:#FFFFFF;', r'fill:transparent;', svg_content)

    # 将SVG保存到磁盘
    output_file = os.path.join(output_folder, f"{ipt}.svg")
    with open(output_file, 'w') as file:
        file.write(svg_content)
    
    return output_file

# 应用函数到SMILES列，将结果存储在新的一列中
df['SVG'] = df.apply(lambda row: smiles_to_svg(row['SMILES'], row['IPT']), axis=1)

print(f"SVG文件已成功保存到 {output_folder} 文件夹中。")