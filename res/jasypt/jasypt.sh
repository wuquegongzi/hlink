# 解密
#java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringDecryptionCLI input=" " password=abc algorithm=PBEWithMD5AndDES

# 加密
java -cp jasypt-1.9.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input="123456" password=TDFlink#2020 algorithm=PBEWithMD5AndDES
