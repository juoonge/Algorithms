#define _CRT_SECURE_NO_WARNINGS
#include <stdio.h>
#include <malloc.h>

// ��������Ʈ�� �̿��Ͽ� �׷��� ������ ��
// ���⼺ �׷��� (from -> to)
struct list { // to list�� ���� 
	int dest;
	struct list* next;
};

struct page { // header node ����
	int num_link;
	double crank; //current rank
	double rank_sum;
	struct list* link;
};
int n, count;

struct page* make_list(FILE*);
bool iterate1(struct page*);
bool iterate2(struct page*);

void main() {
	int i, changed;
	char buf[100];
	FILE* fp;
	struct page* pinfo;

	printf("�Է� ����? ");
	gets_s(buf);

	if ((fp = fopen(buf, "r")) == NULL) {
		printf("%s - ", buf);
		perror("fopen ����");
		return;
	}
	pinfo = make_list(fp);
	fclose(fp);

	// version 1: No dead end
	while (iterate1(pinfo) == true);

	fp = fopen("out1.txt", "w"); // iterate1(pinfo)==false �϶� ����
	for (i = 0; i < n; i++) {
		fprintf(fp, "%5dL %f\n", i, pinfo[i].crank);
	}
	fclose(fp);

	//version 2: Distribute the mass of dead ends to every node evenly
	count = 0;
	double rank = 1.0 / n;
	for (i = 0; i < n; i++) {
		pinfo[i].crank = rank;
	}
	while (iterate2(pinfo) == true);

	fp = fopen("out2.txt", "w");
	for (i = 0; i < n; i++) {
		fprintf(fp, "%5dL %f\n", i, pinfo[i].crank);
	}
	fclose(fp);
}

struct page* make_list(FILE* fp) {
	int from, to;
	double rank;
	struct page* pinfo;
	struct list* node;

	fscanf(fp, "%d", &n); //n=vertext ��
	pinfo = (struct page*)malloc(sizeof(struct page) * n);
	rank = 1.0 / n;
	for (int i = 0; i < n; i++) {
		pinfo[i].num_link = 0;
		pinfo[i].rank_sum = 0.0;
		pinfo[i].crank = rank; // 1/n���� �ʱ�ȭ
		pinfo[i].link = NULL;
	}
	while (fscanf(fp, "%d%d", &from, &to) != EOF) {
		node = (struct list*)malloc(sizeof(struct list));
		node->dest = to;
		node->next = pinfo[from].link;
		pinfo[from].link = node;
		// �� 3������ stack�� push
		pinfo[from].num_link++;
	}
	return pinfo;
}

bool iterate1(struct page* pinfo) {
	int from;
	bool changed = false;
	double mass;
	struct list* ptr;

	for (from = 0; from < n; from++) { // rank�� ������
		if (pinfo[from].num_link == 0) //dead end
			continue;

		mass = pinfo[from].crank / pinfo[from].num_link;
		for (ptr = pinfo[from].link; ptr; ptr = ptr->next)
			pinfo[ptr->dest].rank_sum += mass;
	}

	for (from = 0; from < n; from++) { // crank�� ���� & �˻�
		if (pinfo[from].crank - pinfo[from].rank_sum > 0.00000001 ||
			pinfo[from].rank_sum - pinfo[from].crank > 0.00000001) {
			changed = true;
			pinfo[from].crank = pinfo[from].rank_sum;
		}
		pinfo[from].rank_sum = 0.0;
	}
	printf("Iterate 1: %d��° �ݺ� �Ϸ�\n", ++count);
	return changed;
}

bool iterate2(struct page* pinfo) {
	int from;
	bool changed = false;
	double mass;
	struct list* ptr;

	for (from = 0; from < n; from++) {
		if (pinfo[from].num_link == 0) { // dead end
			mass = pinfo[from].crank / n;
			for (int i = 0; i < n; i++) {
				pinfo[i].rank_sum += mass;
			}
		}
		else {
			mass = pinfo[from].crank / pinfo[from].num_link;
			for (ptr = pinfo[from].link; ptr;
				ptr = ptr->next)
				pinfo[ptr->dest].rank_sum += mass;
		}
	}
	for (from = 0; from < n; from++) {
		if (pinfo[from].crank - pinfo[from].rank_sum > 0.00000001 ||
			pinfo[from].rank_sum - pinfo[from].crank > 0.00000001) {
			changed = true;
			pinfo[from].crank = pinfo[from].rank_sum;
		}
		pinfo[from].rank_sum = 0.0;
	}
	printf("Iterate 2: %d��° �ݺ� �Ϸ�\n", ++count);
	return changed;
}