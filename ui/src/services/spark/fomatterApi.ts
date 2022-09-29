// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

const getList = async function (
  params: {
    name: string;
    type?: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/formatter/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export async function createNew(params: any, options?: { [key: string]: any }) {
  return request<any>('/api/formatter/create_new', {
    method: 'post',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

const getAllFormatterType = async function () {
  return request<API.KeyValue>('/api/inbound/get_all_formatter_type', {
    method: 'GET',
  });
};

const view = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/formatter/view', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

const disable = async function (
  params: {
    name: string;
  },
  options?: { [key: string]: any },
) {
  return request<any>('/api/formatter/disable', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
};

export default {
  getList,
  view,
  disable,
  createNew,
  getAllFormatterType,
};
