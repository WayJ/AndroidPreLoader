def mod1(n):
	fibs=[1,1]
	if n==0|n==1:
		return 1
	else :
		for i in range(n-1):
			fibs.append(fibs[-2]+fibs[-1])
	return fibs


print(mod1(100))
