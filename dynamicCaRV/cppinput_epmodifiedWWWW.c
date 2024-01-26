#line 1 "epmodifiedWWWW.c"
#pragma startinclude #include <stdio.h>
#line 1
#include <stdio.h>
#pragma endinclude
#line 2
#pragma startinclude #include <stdlib.h>
#line 2
#include <stdlib.h>
#pragma endinclude
#line 3
#pragma startinclude #include <math.h>
#line 3
#include <math.h>
#pragma endinclude
#line 4
#pragma startinclude #include <time.h>
#line 4
#include <time.h>
#pragma endinclude
#line 5
#ifndef DOS
#pragma startinclude #include <sys/time.h>
#line 6
#include <sys/time.h>
#pragma endinclude
#line 7
#endif
#ifndef __TYPE_H__
#define __TYPE_H__
typedef enum { false, true } logical;
typedef struct { 
  double real;
  double imag;
} dcomplex;
#define min(x,y)    ((x) < (y) ? (x) : (y))
#define max(x,y)    ((x) > (y) ? (x) : (y))
#endif
#define CLASS  'W'
#define M      25
#define CONVERTDOUBLE  false
#define COMPILETIME "29 Nov 2021"
#define NPBVERSION "3.3.1"
#define CS1 "gcc"
#define CS2 "$(CC)"
#define CS3 "-lm"
#define CS4 "-I../common"
#define CS5 "-g -Wall -O3 -mcmodel=medium"
#define CS6 "-O3 -mcmodel=medium"
#define CS7 "randdp"
#ifndef __RANDDP_H__
#define __RANDDP_H__
double randlc( double *x, double a );
void vranlc( int n, double *x, double a, double y[] );
#endif
#ifndef __TIMERS_H__
#define __TIMERS_H__
void timer_clear( int n );
void timer_start( int n );
void timer_stop( int n );
double timer_read( int n );
#endif
#ifndef __PRINT_RESULTS_H__
#define __PRINT_RESULTS_H__
void print_results(char *name, char class, int n1, int n2, int n3, int niter,
    double t, double mops, char *optype, logical verified, char *npbversion,
    char *compiletime, char *cs1, char *cs2, char *cs3, char *cs4, char *cs5,
    char *cs6, char *cs7);
#endif
#if defined(IBM)
#define wtime wtime
#elif defined(CRAY)
#define wtime WTIME
#else
#define wtime wtime_
#endif
#define MAX(X,Y)  (((X) > (Y)) ? (X) : (Y))
#define MK        16
#define MM        (M - MK)
#define NN        (1 << MM)
#define NK        (1 << MK)
#define NQ        10
#define EPSILON   1.0e-8
#define A         1220703125.0
#define S         271828183.0
static double x[2*NK];
static double q[NQ]; 
int main() 
{
  double Mops, t1, t2, t3, t4, x1, x2;
  double sx, sy, tm, an, tt, gc;
  double sx_verify_value, sy_verify_value, sx_err, sy_err;
  int    np;
  int    i, ik, kk, l, k, nit;
  int    k_offset, j;
  logical verified, timers_enabled;
  double dum[3] = {1.0, 1.0, 1.0};
  char   size[16];
  FILE *fp;
  if ((fp = fopen("timer.flag", "r")) == NULL) {
    timers_enabled = false;
  } else {
    timers_enabled = true;
    fclose(fp);
  }
  sprintf(size, "%15.0lf", pow(2.0, M+1));
  j = 14;
  if (size[j] == '.') j--;
  size[j+1] = '\0';
  printf("\n\n NAS Parallel Benchmarks (NPB3.3-SER-C) - EP Benchmark\n");
  printf("\n Number of random numbers generated: %15s\n", size);
  verified = false;
  np = NN; 
  vranlc(0, &dum[0], dum[1], &dum[2]);
  dum[0] = randlc(&dum[1], dum[2]);
  for (i = 0; i < 2 * NK; i++) {
    x[i] = -1.0e99;
  }
  Mops = log(sqrt(fabs(MAX(1.0, 1.0))));   
  timer_clear(0);
  timer_clear(1);
  timer_clear(2);
  timer_start(0);
  t1 = A;
  vranlc(0, &t1, A, x);
  t1 = A;
  for (i = 0; i < MK + 1; i++) {
    t2 = randlc(&t1, t1);
  }
  an = t1;
  tt = S;
  gc = 0.0;
  sx = 0.0;
  sy = 0.0;
  for (i = 0; i < NQ; i++) {
    q[i] = 0.0;
  }
  k_offset = -1;
  for (k = 1; k <= np; k++) {
    kk = k_offset + k; 
    t1 = S;
    t2 = an;
    for (i = 1; i <= 100; i++) {
      ik = kk / 2;
      if ((2 * ik) != kk) t3 = randlc(&t1, t2);
      if (ik == 0) break;
      t3 = randlc(&t2, t2);
      kk = ik;
    }
    if (timers_enabled) timer_start(2);
    vranlc(2 * NK, &t1, A, x);
    if (timers_enabled) timer_stop(2);
    if (timers_enabled) timer_start(1);
#pragma experimental section start
    for (i = 0; i < NK; i++) {
      x1 = 2.0 * x[2*i] - 1.0;
      x2 = 2.0 * x[2*i+1] - 1.0;
      t1 = x1 * x1 + x2 * x2;
      if (t1 <= 1.0) {
        t2   = sqrt(-2.0 * log(t1) / t1);
        t3   = (x1 * t2);
        t4   = (x2 * t2);
        l    = MAX(fabs(t3), fabs(t4));
        q[l] = q[l] + 1.0;
        sx   = sx + t3;
        sy   = sy + t4;
      }
    }
#pragma experimental section stop
    if (timers_enabled) timer_stop(1);
  }
  for (i = 0; i < NQ; i++) {
    gc = gc + q[i];
  }
  timer_stop(0);
  tm = timer_read(0);
  nit = 0;
  verified = true;
  if (M == 24) {
    sx_verify_value = -3.247834652034740e+3;
    sy_verify_value = -6.958407078382297e+3;
  } else if (M == 25) {
    sx_verify_value = -2.863319731645753e+3;
    sy_verify_value = -6.320053679109499e+3;
  } else if (M == 28) {
    sx_verify_value = -4.295875165629892e+3;
    sy_verify_value = -1.580732573678431e+4;
  } else if (M == 30) {
    sx_verify_value =  4.033815542441498e+4;
    sy_verify_value = -2.660669192809235e+4;
  } else if (M == 32) {
    sx_verify_value =  4.764367927995374e+4;
    sy_verify_value = -8.084072988043731e+4;
  } else if (M == 36) {
    sx_verify_value =  1.982481200946593e+5;
    sy_verify_value = -1.020596636361769e+5;
  } else if (M == 40) {
    sx_verify_value = -5.319717441530e+05;
    sy_verify_value = -3.688834557731e+05;
  } else {
    verified = false;
  }
  if (verified) {
    sx_err = fabs((sx - sx_verify_value) / sx_verify_value);
    sy_err = fabs((sy - sy_verify_value) / sy_verify_value);
    verified = ((sx_err <= EPSILON) && (sy_err <= EPSILON));
  }
  Mops = pow(2.0, M+1) / tm / 1000000.0;
  printf("\nEP Benchmark Results:\n\n");
  printf("CPU Time =%10.4lf\n", tm);
  printf("N = 2^%5d\n", M);
  printf("No. Gaussian Pairs = %15.0lf\n", gc);
  printf("Sums = %25.15lE %25.15lE\n", sx, sy);
  printf("Counts: \n");
  for (i = 0; i < NQ; i++) {
    printf("%3d%15.0lf\n", i, q[i]);
  }
  print_results("EP", CLASS, M+1, 0, 0, nit,
      tm, Mops, 
      "Random numbers generated",
      verified, NPBVERSION, COMPILETIME, CS1,
      CS2, CS3, CS4, CS5, CS6, CS7);
  if (timers_enabled) {
    if (tm <= 0.0) tm = 1.0;
    tt = timer_read(0);
    printf("\nTotal time:     %9.3lf (%6.2lf)\n", tt, tt*100.0/tm);
    tt = timer_read(1);
    printf("Gaussian pairs: %9.3lf (%6.2lf)\n", tt, tt*100.0/tm);
    tt = timer_read(2);
    printf("Random numbers: %9.3lf (%6.2lf)\n", tt, tt*100.0/tm);
  }
  return 0;
}
void print_results(char *name, char class, int n1, int n2, int n3, int niter,
    double t, double mops, char *optype, logical verified, char *npbversion,
    char *compiletime, char *cs1, char *cs2, char *cs3, char *cs4, char *cs5,
    char *cs6, char *cs7) 
{
  char size[16];
  int j;
  printf( "\n\n %s Benchmark Completed.\n", name );
  printf( " Class           =             %12c\n", class );
  if ( ( n2 == 0 ) && ( n3 == 0 ) ) {
    if ( ( name[0] == 'E' ) && ( name[1] == 'P' ) ) {
      sprintf( size, "%15.0lf", pow(2.0, n1) );
      j = 14;
      if ( size[j] == '.' ) {
        size[j] = ' '; 
        j--;
      }
      size[j+1] = '\0';
      printf( " Size            =          %15s\n", size );
    } else {
      printf( " Size            =             %12d\n", n1 );
    }
  } else {
    printf( " Size            =           %4dx%4dx%4d\n", n1, n2, n3 );
  }
  printf( " Iterations      =             %12d\n", niter );
  printf( " Time in seconds =             %12.2lf\n", t );
  printf( " Mop/s total     =          %15.2lf\n", mops );
  printf( " Operation type  = %24s\n", optype );
  if ( verified ) 
    printf( " Verification    =             %12s\n", "SUCCESSFUL" );
  else
    printf( " Verification    =             %12s\n", "UNSUCCESSFUL" );
  printf( " Version         =             %12s\n", npbversion );
  printf( " Compile date    =             %12s\n", compiletime );
  printf( "\n Compile options:\n"
          "    CC           = %s\n", cs1 );
  printf( "    CLINK        = %s\n", cs2 );
  printf( "    C_LIB        = %s\n", cs3 );
  printf( "    C_INC        = %s\n", cs4 );
  printf( "    CFLAGS       = %s\n", cs5 );
  printf( "    CLINKFLAGS   = %s\n", cs6 );
  printf( "    RAND         = %s\n", cs7 );
  printf( "\n--------------------------------------\n"
          " Please send all errors/feedbacks to:\n"
          " Center for Manycore Programming\n"
          " cmp@aces.snu.ac.kr\n"
          " http://aces.snu.ac.kr\n"
          "--------------------------------------\n\n");
}
double randlc( double *x, double a )
{
  const double r23 = 1.1920928955078125e-07;
  const double r46 = r23 * r23;
  const double t23 = 8.388608e+06;
  const double t46 = t23 * t23;
  double t1, t2, t3, t4, a1, a2, x1, x2, z;
  double r;
  t1 = r23 * a;
  a1 = (int) t1;
  a2 = a - t23 * a1;
  t1 = r23 * (*x);
  x1 = (int) t1;
  x2 = *x - t23 * x1;
  t1 = a1 * x2 + a2 * x1;
  t2 = (int) (r23 * t1);
  z = t1 - t23 * t2;
  t3 = t23 * z + a2 * x2;
  t4 = (int) (r46 * t3);
  *x = t3 - t46 * t4;
  r = r46 * (*x);
  return r;
}
void vranlc( int n, double *x, double a, double y[] )
{
  const double r23 = 1.1920928955078125e-07;
  const double r46 = r23 * r23;
  const double t23 = 8.388608e+06;
  const double t46 = t23 * t23;
  double t1, t2, t3, t4, a1, a2, x1, x2, z;
  int i;
  t1 = r23 * a;
  a1 = (int) t1;
  a2 = a - t23 * a1;
  for ( i = 0; i < n; i++ ) {
    t1 = r23 * (*x);
    x1 = (int) t1;
    x2 = *x - t23 * x1;
    t1 = a1 * x2 + a2 * x1;
    t2 = (int) (r23 * t1);
    z = t1 - t23 * t2;
    t3 = t23 * z + a2 * x2;
    t4 = (int) (r46 * t3) ;
    *x = t3 - t46 * t4;
    y[i] = r46 * (*x);
  }
  return;
}
static double elapsed_time( void )
{
    double t;
    wtime( &t );
    return( t );
}
static double start[64], elapsed[64];
void timer_clear( int n )
{
    elapsed[n] = 0.0;
}
void timer_start( int n )
{
    start[n] = elapsed_time();
}
void timer_stop( int n )
{
    double t, now;
    now = elapsed_time();
    t = now - start[n];
    elapsed[n] += t;
}
double timer_read( int n )
{
    return( elapsed[n] );
}
void wtime(double *t)
{
  static int sec = -1;
  struct timeval tv;
  gettimeofday(&tv, (void *)0);
  if (sec < 0) sec = tv.tv_sec;
  *t = (tv.tv_sec - sec) + 1.0e-6*tv.tv_usec;
}
